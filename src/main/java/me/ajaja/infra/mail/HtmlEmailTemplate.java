package me.ajaja.infra.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class HtmlEmailTemplate {
	private static final String BASIC_FORM = """
			<table border="0" cellpadding="0" cellspacing="0" width="100%%" id="bodyTable">
		       <tr>
		         <td align="center">
		           <table border="1" cellpadding="0" cellspacing="0" width="600" style="border-style: solid; padding: 20px; border-top-color: #f76c5e; border-width: 2px; border-top-width: 12px">
		             <tr>
		               <td style="border: none">
		                 <img src="https://i.imgur.com/vmHDigp.png" alt="올해도 아좌좌 LOGO" width="140" height="80" />
		               </td>
		             </tr>
		             %s
		             <tr>
		               <td style="border: none; box-sizing: border-box; padding-top: 18px"><hr /></td>
		             </tr>
		             <tr>
					   <td style="border: none; box-sizing: border-box; padding-top: 12px">
						 <div style="font-size: 14px">
						   문의사항이 있다면 다음 이메일로 연락 바랍니다!<br/>
						   E-mail: gmlwh124@naver.com
						 </div>
					   </td>
					 </tr>
		           </table>
		         </td>
		       </tr>
		     </table>
		""";

	@RequiredArgsConstructor
	enum MailType {
		EMAIL_VERIFICATION("[올해도 아좌좌🔥] 이메일 인증을 진행해 주세요!", """
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; border: none; font-size: 18px; font-weight: 600"><font>📧 E-mail 인증 요청입니다.</font></td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 12px"><hr /></td>
					 </tr>
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; border: none">
			         		<font> 인증 번호 : <font color="#f76c5e" style="font-weight: 600">%s</font></font>
			         		<div style="font-size: 14px; padding-top: 16px">
			         		인증 화면에 6자리 인증 번호를 입력해 주세요!<br/>
			         		인증 번호는 <font color="#f76c5e">10분 동안만 유효</font>합니다.
			         		</div>
				   		</td>
					</tr>
			"""),
		REMIND("[올해도 아좌좌🔥] \"%s\" 계획 잘 지키고 계신가요?! 리마인드를 확인하세요👻", """
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; border: none; font-size: 20px; font-weight: 600"><font>🔥 리마인드 메세지입니다.</font></td>
			         </tr>
			         <tr>
			           		<td style="border: none; padding-top: 12px"><hr /></td>
			         </tr>
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; padding-left: 4px; border: none">
			         		<font color="#f76c5e" style="font-weight: 600">%s</font> 계획에 대한 리마인드 입니다.
			         	</td>
			         </tr>
			         <tr>
			         	<td style="border: none; box-sizing: border-box; padding-top: 16px">
			         		<div style="box-sizing: border-box; padding: 12px; width: 100%%; height: 300px; border-style: solid; border-radius: 12px; border-color: #feceab; border-width: 2px; background-color: #fff6e9;">
			         			<div style="box-sizing: border-box; padding: 12px; height: 100%%; border-style: dashed; border-radius: 12px; border-color: #feceab; border-width: 2px; background-color: #ffffff">
			         	   			<font>%s</font>
			         	 		</div>
							</div>
			          	</td>
			         </tr>
			         <tr>
			         	<td style="border: none; box-sizing: border-box; padding-top: 16px">
			         		<font>해당 계획을 잘 지키고 있으신가요??</font>
						</td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 6px">
			         		<font>계획을 얼마나 잘 지키고 있었는지 <font color="#f76c5e" style="font-weight: 600">%d월 %d일</font>까지 피드백해 볼 수 있어요!</font>
			          	</td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 8px">
			         		<font> 👉</font><a href="%s" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">피드백 하러가기</font></a>
			          	</td>
			         </tr>
			"""),
		AJAJA("[올해도 아좌좌🔥] \"%s\" 계획을 사람들이 응원하고 있어요!!", """
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; border: none; font-size: 20px; font-weight: 600"><font>🎉 응원 메세지 알림입니다.</font></td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 12px"><hr /></td>
			         </tr>
			         <tr>
			         	<td style="box-sizing: border-box; padding-top: 20px; border: none">
			         		지난 주에 나의 <font color="#f76c5e" style="font-weight: 600">%s</font> 계획을 %d명이나 응원했어요 👏👏</br>
			         		<div style="padding-top: 8px; font-size: 14px">이 기세를 몰아 다음 리마인드까지 열심히 노력해볼까요??</div>
			         	</td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 12px; font-size: 14px">
			         		<font> 👉 </font><a href="https://www.ajaja.me/plans/%d" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">내 계획 확인하기</font></a>
						</td>
			         </tr>
			         <tr>
			         	<td style="border: none; padding-top: 8px; font-size: 14px">
			         		<font> 👉 </font><a href="https://www.ajaja.me/explore" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">다른 사람 계획 보러가기</font></a>
			         	</td>
			         </tr>
			""");

		private final String subject;
		private final String content;

		public String subject() {
			if (this.equals(EMAIL_VERIFICATION)) {
				return subject;
			}

			throw new UnsupportedOperationException("This method is for EMAIL VERIFICATION");
		}

		public String subject(String title) {
			if (!this.equals(EMAIL_VERIFICATION)) {
				return subject.formatted(title);
			}

			throw new UnsupportedOperationException("This method is not allowed to EMAIL VERIFICATION");
		}

		public String content(String certification) {
			if (this.equals(EMAIL_VERIFICATION)) {
				return wrapBasicForm(content.formatted(certification));
			}

			throw new UnsupportedOperationException("This method is for EMAIL VERIFICATION");
		}

		public String content(String title, String message, int month, int day, String feedbackUrl) {
			if (this.equals(REMIND)) {
				return wrapBasicForm(content.formatted(title, message, month, day, feedbackUrl));
			}

			throw new UnsupportedOperationException("This method is for REMIND");
		}

		public String content(String title, Long ajajaCount, Long planId) {
			if (this.equals(AJAJA)) {
				return wrapBasicForm(content.formatted(title, ajajaCount, planId));
			}

			throw new UnsupportedOperationException("This method is for AJAJA");
		}

		private String wrapBasicForm(String content) {
			return BASIC_FORM.formatted(content);
		}
	}
}
