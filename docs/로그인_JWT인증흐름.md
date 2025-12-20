# 1 
클라이언트가 카카오 로그인 버튼을 누르는 등<br>
http://localhost:8080/YKiosk/oauth2/authorization/kakao 로 get 요청
<br>저 주소는 application.properties에 미리 정의해놓음
<br>카카오개발자 센터에는 redirect url를 설정

# 2 
사용자는 동의 버튼을 누르는 등 로그인 시도
<br>그러면 카카오 로그인 페이지로 이동함. 이 페이지는 내 서버와 무관

# 3 
정상적으로 로그인 성공한 경우 카카오 개발자센터에 설정한 redirect url로 redirect 받음
<br>http://localhost:8080/YKiosk/login/oauth2/code/kakao
<br>저 url 뒤에 ?code=ㅁㅁㅁㅁ.. 이렇게 코드가 딸려옴
<br>/YKiosk/login/oauth2/code/{registerId} 이 형식을 지켜줘야됨.

# 4
OAuth2LoginAuthenticationFilter(스프링 시큐리티 내부 클래스)가 형식에 맞게 3에서 설정한 url을 낚아챔
<br>gradle에 설정한 라이브러리가 자동으로 실행하는 **숨겨진 필터**임.
<br>해당 클래스는 url 뒤에 붙어온 code를 뽑아내고 
<br>카카오 서버에 access token을 요청함.
<br>토큰을 받으면 이걸로 카카오 서버에 User Info를 요청
<br>그러면 내가 만든 SuccessHandler(OAuth2LoginSuccessHandler)를 호출함. 

# 5
OAuth2LoginSuccessHandler의 onAuthenticationSuccess가 실행됨
<br>메소드 내에서 jwtTokenProvider로 accessToken을 발급하고
<br>내가 설정한 redirect url(코드에선 targetUrl 이라는 변수명 사용. 일단 /main으로 보냄)로 리다이렉트.

# 6
5의 JwtTokenProvider의 createAccessToken로 토큰 발급
<br>여기까지 무사히 되면 브라우저는 쿠키(JWT)를 저장하고 /YKiosk/main으로 이동함

# 7
5에서 redirect url을 클라이언트에게 전송.
<br>그러면 클라이언트의 브라우저가 받은 redirect url을 가지고 서버에 요청함. 이 때 받은 쿠키(JWT)도 같이 들고감.
<br>요청이 오면 가장 먼저 보안설정파일인 SecurityConfig 를 확인함.
<br>SecurityConfig.SecurityFilterChain에 addFilterBefore 설정이 있어서 다른 필터보다 여기서 설정한 필터를 먼저 확인함.
<br>즉 해당 url을 담당하는 컨트롤러의 메소드에 도착하기 전에 JwtAuthenticationFilter가 먼저 가로채서 검증함.

# 8
SecurityConfig의 설정대로 JwtAuthenticationFilter.doFilterInternal를 통해 토큰을 검사함.
<br>JwtTokenProvider.validateToken를 통해 토큰이 위조되진 않았는지, 유효기간이 지났는지 확인하고 true/false를 줌.
<br>JwtTokenProvider.getAuthentication르 이용해 스프링 시큐리티용 신분증객체(Authentication)를 만들어줌

# 9
다시 SecurityConfig로 가서 필터를 통과한 요청이 권한이 있는지 확인함.
<br>현재는 코드를 수정하여 컨트롤러에서 권한에 따라 접근제한을 걸어둠

# 10 
검증과정이 끝나면 컨트롤러로 가서 기능 수행

**여기까진 spring boot만 쓰는 경우에 해당**
<br>React를 쓰는 경우는 로직이 달라짐.

# spring boot만 쓰는 경우
1. 사용자가 카카오 로그인 시도
2. redirect url에 code값을 추가해서 줌.
3. code값을 가지고 카카오에 access token을 달라고 함.
4. 해당 토큰을 가지고 카카오로부터 사용자 정보를 가져올 수 있음(3,4 는 스프링 시큐리티의 클래스가 자동으로 처리).
5. 받은 사용자 정보를 가지고 서버의 사용자가 맞는지 검증을 가짐.
6. 검증된 사용자면 redirect url을 처리하는 메소드에서 특정 페이지를 반환.

# React를 같이 쓰는 경우
1. react에서 연 웹페이지로 카카오 로그인 시도
2. 카카오로부터의 redirect는 기존의 spring boot 서버로 redirect 받음
3. 기존의 만든 로직에 따라 카카오와 access token을 주고받고, 사용자 정보를 얻음. 사용자 정보로 jwt 발급
4. 발급받고 나면 redirect를 react 서버로 보냄

<br>React를 도입하면서 SecurityConfig에 CORS 설정만 추가해줌
<br>그러면 웹에서는 axios로 url 쓸 때 withCredentials 값을 true로 해줘야 JWT가 서버로 날라감.
<br>사용자 인증은 스프링 시큐리티가 알아서 하니까 Controller든 RestController든 사용자 인증은 신경 안써도 됨.
<br>컨트롤러에서는 파라미터로 Authentication 객체를 받아 이걸로 사용자의 정보를 확인할 수 있음.
<br>위에처럼 하면 받은 객체를 UserDetail로 한번 변환해야됨.
<br>그래서 (@AuthenticationPrincipal UserDetails userDetails) 이렇게 파라미터를 받으면 바로 사용자 정보를 쓸 수 있음.
