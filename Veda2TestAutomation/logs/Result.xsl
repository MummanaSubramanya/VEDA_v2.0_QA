<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited by XMLSpy® -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" version="1.0" encoding="UTF-8" />
<xsl:template match="/">
  <html>
  <head>
<script src="jquery-1.9.1.min.js"></script>
<script src="highcharts.js"></script>
<script src="exporting.js"></script>
<script> 
var pass_count = <xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Pass"])'/>
var fail_count = <xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Fail"])'/>

$(function () {
    $('#container').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: true
        },
        title: {
           text:''
        },
        tooltip: {
    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    color: '#000000',
                    connectorColor: '#000000',
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        series: [{
            type: 'pie',
            name: 'Test Results',
            data: [
                    
					['Pass',   pass_count],
	               ['Fail',fail_count]
            ]
        }]
    });
	
});
    

$(document).ready(function(){
  $(".flip").click(function(){
  var selector = "#panel"+$(this).attr('id');
  var selector1 = ".panel"+$(this).attr('id');
  $(selector1).show();
  $(selector).slideToggle("slow",function(){
    if ($(selector).css('display') == 'none')
  {
	$(selector1).hide();
  }
  });
  
  });
  
  $(".flip1").click(function(){
  var selector = "#panel1"+$(this).attr('id');
  var selector1 = ".panel1"+$(this).attr('id');
  $(selector1).show();
  $(selector).slideToggle("slow",function(){
    if ($(selector).css('display') == 'none')
  {
	$(selector1).hide();
  }
  });
  
  });
  $(".flip2").click(function(){
  var selector = "#panel2"+$(this).attr('id');
  var selector1 = ".panel2"+$(this).attr('id');
  $(selector1).show();
  $(selector).slideToggle("slow",function(){
    if ($(selector).css('display') == 'none')
  {
	$(selector1).hide();
  }
  });
  
  });
});

function Fail_Status(){
  
  $("#fail").toggle();
  $("#Success").hide();
	if ($("#fail").is(":hidden")) {
    $("#main").show();
  } else {
      $("#main").hide();
}
}
function Success_Status(){
  
  $("#Success").toggle();
  $("#fail").hide();
  if ($("#Success").is(":hidden")) {
    $("#main").show();
  } else {
      $("#main").hide();
}
  /*var selector = "#panel"+$(this).attr('id');
  var selector1 = ".panel"+$(this).attr('id');
  $(selector).slideToggle("slow",function(){
    if ($(selector).css('display') == 'none')
  {
	$(selector1).hide();
  }*/
}

</script>
<style type="text/css"> 
.panel
{
display:none;
}
.test
 {
	width:989px ;
 } 
 
 th{
	font-family:"Calibri";
	FONT-SIZE: 12pt;
	color:#FFFFFF;
 }
 td{
	font-family:"Calibri";
	FONT-SIZE: 12pt;
 }
  .maintable{
  width:1000px;
  
 }
 .step
 {
	width:50px;
 }
 .Sname
 {
	width:200px;
 }
 
 .Tstamp
 {
	width:120px;
 }
</style>
 
<style>
	/*H3 {FONT-SIZE: 17pt; COLOR: #008000;text-align:center;}
	body{
	background-color: #FFFFFF;background-image:url('frugal_header.png');background-repeat:no-repeat;background-position:right top;background-size:450px 175px;;
	}*/
	/*@-moz-document url-prefix() {
	.frugal{
	margin:0;float:right;width:380px;height:155px;
	}
	}*/
	.frugal{
	margin:0;float:right;width:380px;height:155px;
	}
</style>
</head>
  <body>  
	<table align="center">
		<tr>
			<td>
				<h3 style="background:#808080;color:#FFFFFF;margin-left:31%;margin-right:31%;padding-left:20px;"><font face="Calibri" size="4">Summary</font></h3>
				<table border="1" align="left" cellpadding="5" >
					<tr>
						<td align="center"><font face="Calibri" size="2" color="#000000"><b>No. of Functionalities</b></font></td>
						<td align="center" width="10%"><output><xsl:value-of select='count(//TestSuite/Functionality)'/></output></td>
						<td align="center"><font face="Calibri" size="2" color="#000000"><b>No. of Test cases</b></font></td>
						<td align="center"><output><xsl:value-of select='count(//TestSuite/Functionality/TestScript)'/></output></td>
					</tr>
					<tr>
						<td align="center"><strong><font color="green" face="Calibri" size="2">Test cases Passed</font></strong></td>
						<td align="center" bgcolor="#9acd32"><output><a href="#" onClick="Success_Status();"><xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Pass"])'/></a></output></td>
						<xsl:variable name="pass_count"><xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Pass"])'/></xsl:variable>
						<td	align="center"><strong><font color="red" face="Calibri" size="2">Test cases Failed</font></strong></td>
						<td align="center" bgcolor="red"><output><a href="#" onClick="Fail_Status();"> <xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Fail"])'/></a></output></td>
						<xsl:variable name="fail_count"><xsl:value-of select='count(//TestSuite/Functionality/TestScript[@TC_Status="Fail"])'/></xsl:variable>
					</tr>
				</table>
			</td>
			<td>
			<h3 style="background:#808080;color:#FFFFFF;margin-left:37%;margin-right:37%;padding-left:20px;"><font face="Calibri" size="4">Pie Chart</font></h3>
				<div id="container" style="min-width: 400px; height: 300px; margin: 0 auto;align:right;"></div>
			</td>
			
			<img src="frugal_header.png" class="frugal"/>
			
		</tr>
	</table>
			
	
  
  
  <div id="main">
  <h3 style="background:#808080;color:#FFFFFF;margin-left:12%;margin-right:12%;text-align:center"><font face="Calibri" size="4">TestSuite Results</font></h3>
    <table  border="1" align="center" class="maintable">
      <tr bgcolor="#9acd32" style="height:40px">
        <th align="center" width="50px"><b>Functionality</b></th>
        <th align="center"><b>TestScript</b></th>
		<th align="center" width="50px"><b>Status</b></th>
		<th align="center" width="150px"><b>Start Time</b></th>
		<th align="center" width="150px"><b>End Time</b></th>
      </tr>
      <xsl:for-each select="TestSuite/Functionality">
	  <xsl:variable name="functionalityName"><xsl:value-of select="@name"/></xsl:variable>
	  <xsl:variable name="num"><xsl:value-of select="position()" /></xsl:variable>
	  <xsl:for-each select="TestScript">
	  <xsl:variable name="num1"><xsl:value-of select="$num" /><xsl:value-of select="position()" /></xsl:variable>
      <tr>
		<td align="center"><font face="Calibri" size="2" ><b><xsl:value-of select="$functionalityName"/></b></font></td>
		<td class="flip"><xsl:attribute name="id"><xsl:value-of select="$num1"/></xsl:attribute><font face="Calibri" size="2"><b><xsl:value-of select="@name"/></b></font></td>
		<xsl:choose>
			<xsl:when test="@TC_Status='Fail'">
				<td align="center"><font face="Calibri" size="2" color="red" ><b><xsl:value-of select="@TC_Status"/></b></font></td>
			</xsl:when>
			<xsl:otherwise>
				<td align="center"><font face="Calibri" size="2" color="green"> <b><xsl:value-of select="@TC_Status"/></b></font></td>
			</xsl:otherwise>
		</xsl:choose> 
		<td align="center"><font face="Calibri" size="2" ><b><xsl:value-of select="@StartTime"/></b></font></td>
		<td align="center"><font face="Calibri" size="2" ><b><xsl:value-of select="@EndTime"/></b></font></td>
      </tr>
	  <tr style="display:none;"><xsl:attribute name="class">panel<xsl:value-of select="$num1"/></xsl:attribute>
	  <td colspan="5">
	  <div style="display:none;"><xsl:attribute name="id">panel<xsl:value-of select="$num1"/></xsl:attribute>
	  <table border="1" align="center" class="test" >
		  <tr bgcolor="#DCDCDC">
			<th><font face="Calibri" size="2" color="#000000">Step No</font></th>
			<th><font face="Calibri" size="2" color="#000000">Step Name</font></th>
			<th><font face="Calibri" size="2" color="#000000">Description</font></th>
			<th><font face="Calibri" size="2" color="#000000">Status</font></th>
			<th><font face="Calibri" size="2" color="#000000">Screenshot</font></th>
			<th><font face="Calibri" size="2" color="#000000" >Timestamp</font></th>
		  </tr>
	   <xsl:for-each select="Iterator/step">	
		<tr>
		<td align="center" class="step" ><font face="Calibri" size="2"><xsl:value-of select="@no"/></font></td>
		<td class="Sname"><font face="Calibri" size="2"><xsl:value-of select="stepname" /></font></td>
		<td><font face="Calibri" size="2"><xsl:value-of select="Description"/></font></td> 
		<xsl:choose>
			<xsl:when test="status='Fail'">
				<xsl:variable name="URL">
					<xsl:value-of select="screenshot"/> 
				</xsl:variable>
				<td width="5%" align="center"><font face="Calibri" size="2" color="red"><b><xsl:value-of select="status"/></b></font></td>
				<td width="5%" align="center"><a href="{$URL}" target="_blank">Screenshot</a>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td width="5%" align="center"><font face="Calibri" size="2" color="green"><b><xsl:value-of select="status"/></b></font></td>
				<td width="5%" align="center"></td>			
			</xsl:otherwise>
		</xsl:choose> 
		<td class="Tstamp"><font face="Calibri" size="2"><xsl:value-of select="timestamp"/></font></td>
      </tr>
	   </xsl:for-each>
	   </table>
	   </div>
	   </td>
	   </tr>
	  </xsl:for-each>
      </xsl:for-each>
    </table>
	</div>
	<div id="fail" style="display:none;">
	<h3 style="background:#808080;color:#FFFFFF;margin-left:12%;margin-right:12%;text-align:center"><font face="Calibri" size="4">Failed Test cases</font></h3>
	<table  border="1" align="center" class="maintable">
      <tr bgcolor="#9acd32" style="height:40px">
        <th align="center" width="50px"><b>Functionality</b></th>
        <th align="center"><b>TestScript</b></th>
		<th align="center" width="50px"><b>Status</b></th>
		<th align="center" width="150px"><b>Start Time</b></th>
		<th align="center" width="150px"><b>End Time</b></th>
      </tr>
      <xsl:for-each select="TestSuite/Functionality">
		  <xsl:variable name="functionalityName"><xsl:value-of select="@name"/></xsl:variable>
		  <xsl:variable name="num"><xsl:value-of select="position()" /></xsl:variable>
		  <xsl:for-each select="TestScript">
		  <xsl:variable name="num2"><xsl:value-of select="$num" /><xsl:value-of select="position()" /></xsl:variable>
	  
	  <xsl:choose>
		<xsl:when test="@TC_Status='Fail'">
		<tr>
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="$functionalityName"/></b></font></td>
		<td class="flip1"><xsl:attribute name="id"><xsl:value-of select="$num2"/></xsl:attribute><font face="Calibri" size="2"><b><xsl:value-of select="@name"/></b></font></td>
		<td cellpadding="15" align="center"><font face="Calibri" size="2" color="red"><b><xsl:value-of select="@TC_Status"/></b></font></td>
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="@StartTime"/></b></font></td>
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="@EndTime"/></b></font></td>
		 </tr>
		 </xsl:when>
		</xsl:choose> 
    
	  <tr style="display:none;"><xsl:attribute name="class">panel1<xsl:value-of select="$num2"/></xsl:attribute>
	  <td colspan="5">
	  <div style="display:none;"><xsl:attribute name="id">panel1<xsl:value-of select="$num2"/></xsl:attribute>
	  <table border="1" align="center" class="test" >
		  <tr bgcolor="#DCDCDC">
			<th><font face="Calibri" size="2" color="#000000">Step No</font></th>
			<th><font face="Calibri" size="2" color="#000000">Step Name</font></th>
			<th><font face="Calibri" size="2" color="#000000">Description</font></th>
			<th><font face="Calibri" size="2" color="#000000">Status</font></th>
			<th><font face="Calibri" size="2" color="#000000">Timestamp</font></th>
		  </tr>
	   <xsl:for-each select="Iterator/step">	
		<tr>
		<td align="center" class="step" ><font face="Calibri" size="2"><xsl:value-of select="@no"/></font></td>
		<td class="Sname"><font face="Calibri" size="2"><xsl:value-of select="stepname" /></font></td>
		<td><font face="Calibri" size="2"><xsl:value-of select="Description"/></font></td>
		<xsl:choose>
			<xsl:when test="status='Fail'">
				<td width="5%" align="center"><font face="Calibri" size="2" color="red"><b><xsl:value-of select="status"/></b></font></td>
			</xsl:when>
			<xsl:otherwise>
				<td width="5%" align="center"><font face="Calibri" size="2" color="green"><b><xsl:value-of select="status"/></b></font></td>
			</xsl:otherwise>
		</xsl:choose> 
		<td class="Tstamp"><font face="Calibri" size="2"><xsl:value-of select="timestamp"/></font></td>
		</tr>
	   </xsl:for-each>
	   </table>
	   </div>
	   </td>
	   
	   </tr>
	   
	  </xsl:for-each>
      </xsl:for-each>
	  
    </table>
	</div>
	<div id="Success" style="display:none;margin-top:20px;">
	<h3 style="background:#808080;color:#FFFFFF;margin-left:12%;margin-right:12%;text-align:center"><font face="Calibri" size="4">Passed Test cases</font></h3>
	<table  border="1" align="center" class="maintable">
      <tr bgcolor="#9acd32" style="height:40px">
        <th align="center" width="50px"><b>Functionality</b></th>
        <th align="center"><b>TestScript</b></th>
		<th align="center" width="50px"><b>Status</b></th>
		<th align="center" width="150px"><b>Start Time</b></th>
		<th align="center" width="150px"><b>End Time</b></th>
      </tr>
      <xsl:for-each select="TestSuite/Functionality">
	  
		  <xsl:variable name="functionalityName"><xsl:value-of select="@name"/></xsl:variable>
		  <xsl:variable name="num"><xsl:value-of select="position()" /></xsl:variable>
		  <xsl:for-each select="TestScript">
		  <xsl:variable name="num2"><xsl:value-of select="$num" /><xsl:value-of select="position()" /></xsl:variable>
      <tr>
	  <xsl:choose>
		<xsl:when test="@TC_Status='Pass'">
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="$functionalityName"/></b></font></td>
		<td class="flip2"><xsl:attribute name="id"><xsl:value-of select="$num2"/></xsl:attribute><font face="Calibri" size="2"><b><xsl:value-of select="@name"/></b></font></td>
		<td cellpadding="15" align="center"><font face="Calibri" size="2" color="green"><b><xsl:value-of select="@TC_Status"/></b></font></td>
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="@StartTime"/></b></font></td>
		<td align="center"><font face="Calibri" size="2"><b><xsl:value-of select="@EndTime"/></b></font></td>
		</xsl:when>
		</xsl:choose> 
      </tr>
	  <tr style="display:none;"><xsl:attribute name="class">panel2<xsl:value-of select="$num2"/></xsl:attribute>
	  <td colspan="5">
	  <div style="display:none;"><xsl:attribute name="id">panel2<xsl:value-of select="$num2"/></xsl:attribute>
	 <table border="1" align="center" class="test" >
		  <tr bgcolor="#DCDCDC">
			<th><font face="Calibri" size="2" color="#000000">Step No</font></th>
			<th><font face="Calibri" size="2" color="#000000">Step Name</font></th>
			<th><font face="Calibri" size="2" color="#000000">Description</font></th>
			<th><font face="Calibri" size="2" color="#000000">Status</font></th>
			<th><font face="Calibri" size="2" color="#000000" >Timestamp</font></th>
		  </tr>
	   <xsl:for-each select="Iterator/step">	
		<tr>
		<td align="center" class="step" ><font face="Calibri" size="2"><xsl:value-of select="@no"/></font></td>
		<td class="Sname"><font face="Calibri" size="2"><xsl:value-of select="stepname" /></font></td>
		<td><font face="Calibri" size="2"><xsl:value-of select="Description"/></font></td>
		<xsl:choose>
			<xsl:when test="status='Fail'">
				<td width="5%" align="center"><font face="Calibri" size="2" color="red"><b><xsl:value-of select="status"/></b></font></td>
			</xsl:when>
			<xsl:otherwise>
				<td width="5%" align="center"><font face="Calibri" size="2" color="green"><b><xsl:value-of select="status"/></b></font></td>
			</xsl:otherwise>
		</xsl:choose> 
		<td class="Tstamp"><font face="Calibri" size="2"><xsl:value-of select="timestamp"/></font></td>
      </tr>
	   </xsl:for-each>
	   </table>
	   </div>
	   </td>
	   
	   </tr>
	   
	  </xsl:for-each>
	 
      </xsl:for-each>
	  
    </table>
	
	</div>
	
	
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
