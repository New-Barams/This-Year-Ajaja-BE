package com.newbarams.ajaja.infra.ses.template;

public final class AjajaRemindTemplate {
	public static final String html = """
			<table border="0" cellpadding="0" cellspacing="0" width="100%%" id="bodyTable">
		       <tr>
		         <td align="center">
		           <table border="1" cellpadding="0" cellspacing="0" width="600" style="border-style: solid; padding: 20px; border-top-color: #f76c5e; border-width: 2px; border-top-width: 12px">
		             <tr>
		               <td style="border: none">
		                 <img src="https://velog.velcdn.com/images/minw0_o/post/a0a5a3ba-e081-48b5-8c40-240eeae510a6/image.jpg" alt="올해도 아좌좌 LOGO" width="95" height="75" />
		               </td>
		             </tr>
		 
		             <tr>
		               <td style="box-sizing: border-box; padding-top: 20px; border: none; font-size: 20px; font-weight: 600"><font>[올해도 아좌좌] 응원 메세지 알림</font></td>
		             </tr>
		 
		             <tr>
		               <td style="border: none; padding-top: 12px"><hr /></td>
		             </tr>
		 
		             <tr>
		               <td style="box-sizing: border-box; padding-top: 20px; border: none">
		                 <font>현재 나의</font>
		                 <font color="#f76c5e" style="font-weight: 600">%s</font>
		                 <font>계획을 %d명이 응원하고 있어요 💪🏻 </font>
		               </td>
		             </tr>
		 
		             <tr>
		               <td style="border: none; padding-top: 12px">
		                 <font> =></font>
		                 <a href="https://this-year-ajaja-fe.vercel.app/plans/%d" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">내 계획 확인하기</font></a>
		               </td>
		             </tr>
		 
		             <tr>
		               <td style="border: none; padding-top: 8px">
		                 <font> =></font>
		                 <a href="https://this-year-ajaja-fe.vercel.app/explore" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">다른 사람 계획 보러가기</font></a>
		               </td>
		             </tr>
		 
		             <tr>
		               <td style="border: none; box-sizing: border-box; padding-top: 20px"><hr /></td>
		             </tr>
		 
		             <tr>
		               <td style="border: none; box-sizing: border-box; padding-top: 12px">문의: ajajame@naver.com</td>
		             </tr>
		           </table>
		         </td>
		       </tr>
		     </table>
		""";
}
