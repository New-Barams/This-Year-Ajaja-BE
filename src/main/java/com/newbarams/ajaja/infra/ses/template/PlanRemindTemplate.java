package com.newbarams.ajaja.infra.ses.template;

public final class PlanRemindTemplate {
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
		              <td style="box-sizing: border-box; padding-top: 20px; border: none; font-size: 20px; font-weight: 600"><font>[올해도 아좌좌] 리마인드 메세지 알림</font></td>
		            </tr>
		  
		            <tr>
		              <td style="border: none; padding-top: 12px"><hr /></td>
		            </tr>
		  
		            <tr>
		              <td style="box-sizing: border-box; padding-top: 20px; padding-left: 4px; border: none">
		                <font color="#f76c5e" style="font-weight: 600">%s</font>
		                <font>계획에 대한 리마인드 메세지입니다.</font>
		              </td>
		            </tr>
		  
		            <tr>
		              <td style="border: none; box-sizing: border-box; padding-top: 16px">
		                <div
		                  style="
		                    box-sizing: border-box;
		                    padding: 12px;
		                    width: 100%%;
		                    height: 300px;
		                    border-style: solid;
		                    border-radius: 12px;
		                    border-color: #feceab;
		                    border-width: 2px;
		                    background-color: #fff6e9;
		                  "
		                >
		                  <div style="box-sizing: border-box; padding: 12px; height: 100%%; border-style: dashed; border-radius: 12px; border-color: #feceab; border-width: 2px; background-color: #ffffff">
		                    <font
		                      >%s </font
		                    >
		                  </div>
		                </div>
		              </td>
		            </tr>
		            <tr>
		              <td style="border: none; box-sizing: border-box; padding-top: 16px"><font>해당 계획을 잘 지키고 있으신가요 ?</font></td>
		            </tr>
		  
		            <tr>
		              <td style="border: none; padding-top: 4px">
		                <font>계획을 얼마나 잘 지키고 있는지 <font color="#f76c5e" style="font-weight: 600">%d월 %d일</font>까지 피드백해주세요</font>
		              </td>
		            </tr>
		  
		            <tr>
		              <td style="border: none; padding-top: 8px">
		                <font> =></font>
		                <a href="%s" target="_blank" style="color: #f76c5e"><font color="#f76c5e" style="font-weight: 600">피드백 하러가기</font></a>
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
