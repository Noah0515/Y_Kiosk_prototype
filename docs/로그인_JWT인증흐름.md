#1 
클라이언트가 카카오 로그인 버튼을 누르는 등<br>
http://localhost:8080/YKiosk/oauth2/authorization/kakao 로 get 요청
<br>저 주소는 application.properties에 미리 정의해놓음
<br>카카오개발자 센터에는 redirect url를 설정

#2 
사용자는 동의 버튼을 누르는 등 로그인 시도
<br>그러면 카카오 로그인 페이지로 이동함. 이 페이지는 내 서버와 무관

#3 
정상적으로 로그인 성공한 경우 카카오 개발자센터에 설정한 redirect url로 redirect 받음
<br>http://localhost:8080/YKiosk/login/oauth2/code/kakao
<br>저 url 뒤에 ?code=ㅁㅁㅁㅁ.. 이렇게 코드가 딸려옴
<br>/YKiosk/login/oauth2/code/{registerId} 이 형식을 지켜줘야됨.

#4
OAuth2LoginAuthenticationFilter(스프링 시큐리티 내부 클래스)가 형식에 맞게 3에서 설정한 url을 낚아챔
<br>gradle에 설정한 라이브러리가 자동으로 실행하는 **숨겨진 필터**임.
<br>해당 클래스는 url 뒤에 붙어온 code를 뽑아내고 
<br>카카오 서버에 access token을 요청함.
<br>토큰을 받으면 이걸로 카카오 서버에 User Info를 요청
<br>그러면 내가 만든 SuccessHandler(OAuth2LoginSuccessHandler)를 호출함. 

#5
OAuth2LoginSuccessHandler의 onAuthenticationSuccess가 실행됨
<br>메소드 내에서 jwtTokenProvider로 accessToken을 발급하고
<br>내가 설정한 redirect url(코드에선 targetUrl 이라는 변수명 사용. 일단 /main으로 보냄)로 리다이렉트.

#6
5의 JwtTokenProvider의 createAccessToken로 토큰 발급
<br>여기까지 무사히 되면 브라우저는 쿠키(JWT)를 저장하고 /YKiosk/main으로 이동함

#7
5에서 redirect url을 클라이언트에게 전송.
<br>그러면 클라이언트의 브라우저가 받은 redirect url을 가지고 서버에 요청함. 이 때 받은 쿠키(JWT)도 같이 들고감.
<br>요청이 오면 가장 먼저 보안설정파일인 SecurityConfig 를 확인함.
<br>SecurityConfig.SecurityFilterChain에 addFilterBefore 설정이 있어서 다른 필터보다 여기서 설정한 필터를 먼저 확인함.
<br>즉 해당 url을 담당하는 컨트롤러의 메소드에 도착하기 전에 JwtAuthenticationFilter가 먼저 가로채서 검증함.

#8
SecurityConfig의 설정대로 JwtAuthenticationFilter.doFilterInternal를 통해 토큰을 검사함.
<br>JwtTokenProvider.validateToken를 통해 토큰이 위조되진 않았는지, 유효기간이 지났는지 확인하고 true/false를 줌.
<br>JwtTokenProvider.getAuthentication르 이용해 스프링 시큐리티용 신분증객체(Authentication)를 만들어줌

#9
다시 SecurityConfig로 가서 필터를 통과한 요청이 권한이 있는지 확인함.
<br>현재는 코드를 수정하여 컨트롤러에서 권한에 따라 접근제한을 걸어둠

#10 
검증과정이 끝나면 컨트롤러로 가서 기능 수행
