<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>智能提示框</title>
<style type="text/css">
#mydiv{
	position:absolute;
	left:50%;
	top:50%;
	margin-left:-200px;
	margin-top:-50px;
}
.mouseOver{
	background:#708090;
	color:#FFFAFA;
}
.mouseOut{
	background:#FFFAFA;
	color:#000000;
}


</style>
<script type="text/javascript">
	var xmlHttp;
	function getMoreContent(){
		var content=document.getElementById("keyword");
		if(""==content.value){
			clearContent();
			return;
		}
		xmlHttp=createXmlHttp(); 
		var url="search?keyword="+escape(content.value);
		//true表示发送的异步请求
		xmlHttp.open("GET",url,true)
		xmlHttp.onreadystatechange=callback;
		xmlHttp.send(null);
	}
	//回调函数
	function callback(){
		//4代表成功
		if(xmlHttp.readyState==4){
			//200表示浏览器响应成功
			if(xmlHttp.status==200){
				var result=xmlHttp.responseText;
				var json=eval("("+result+")");
				setContent(json);
			}
		}
	}
	//设置数据
	function setContent(contents){
		clearContent();
		setLocation();
		var size=contents.length;
		for(var i=0;i<size;i++){
			var nextNode=contents[i];
			var tr=document.createElement("tr");
			var td=document.createElement("td");
			td.setAttribute("border","0");
			td.setAttribute("bgcolor","#FFFAFA")
			td.onmouseover=function(){
				this.className="mouseOver";
			};
			td.onmouseout=function(){
				this.className="mouseOut";
			};
			td.onclick=function(){
				
			};
			var text=document.createTextNode(nextNode);
			td.appendChild(text);
			tr.appendChild(td);
			document.getElementById("content_table_body").appendChild(tr);
		}
	}
	//获得xmlHttp对象
	function createXmlHttp(){
		var xmlHttp;
		if(window.XMLHttpRequest){
			xmlHttp=new XMLHttpRequest();
		}
		//考虑浏览器的兼容性
		if(window.ActiveXObject){
			xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		    if(!xmlHttp){
		    	xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		    }
		}
		return xmlHttp;
	}
	//清空之前的数据
	function clearContent(){
		var contentTableBody=document.getElementById("content_table_body");
		var size=contentTableBody.childNodes.length;
		for(var i=size-1;i>=0;i--){
			contentTableBody.removeChild(contentTableBody.childNodes[i]);
		}
		document.getElementById("popDiv").style.border="none";
	}
	function setLocation(){
		var content=document.getElementById("keyword");
		var width=content.offsetWidth;//输入框的宽度
		var left=content["offsetLeft"];//到左边框的距离
		var top=content["offsetTop"]+content.offsetHeight;//到顶部的距离
		//获得显示框
		var popDiv=document.getElementById("popDiv");
		popDiv.style.border="black 1px solid";
		popDiv.style.left=left+"px";
		popDiv.style.top=top+"px";
		popDiv.style.width=width+"px";
		document.getElementById("content_table").style.width=width+"px";
		
		
	}
</script>
</head>
<body>
	<div id="mydiv">
	<input type="text"  size="50px" id="keyword" onkeyup="getMoreContent()" onblur="clearContent()" onfocus="getMoreContent()"/>
	<input type="button" value="百度一下" width="50px"/>
	<div id="popDiv">
		<table id="content_table" bgcolor="#FFFAFA" border="0" cellspacing="0" cellpadding="0">
			<tbody id="content_table_body">
			<tr>
				
			</tr>
			</tbody>
		</table>
	</div>
	</div>
</body>
</html>