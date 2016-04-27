<%@ page language="java" contentType="text/html; charset=UTF-8"
	 pageEncoding="UTF-8"%>
<script
	src="<%=request.getContextPath()%>/jsp/third-party/jquery-1.10.2.js"></script>
<script
	src="<%=request.getContextPath()%>/jsp/third-party/ajaxfileupload.js"></script>
	
 <script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jsp/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jsp/ueditor.all.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jsp/lang/zh-cn/zh-cn.js"></script>
<style>
</style>
<body>
	<script id="editor" type="text/plain" style="width:1024px;height:500px;"></script>

	<form id="uploadform" action="UploadServlet" method="post"
		enctype="multipart/form-data">
		<input id="fileupload_bt" type="file"  multiple="multiple"/> 
		
		<fieldset id="filelist_fs" style="width:600px;">
			
		</fieldset>
		
		<input id="buttonUpload" type="button" value="sub" />
	</form>
</body>


<script type="text/javascript">

	var ue = UE.getEditor('editor');
	$('#fileupload_bt').bind(
			'click',
			function() {
				
			})

	$('#buttonUpload').bind('click', function() {
		$.ajaxFileUpload({
			url : 'http://localhost:8080/fileuploader/UploadServlet',
			secureuri : false,
			fileElementId : [ 'file0', 'file1', 'file2' ],//file标签的id  
			dataType : 'json',//返回数据的类型  
			data : {
				name : 'logan'
			},//一同上传的数据  
			success : function(data, status) {
				alert(data.a);
			},
			error : function(data, status, e) {
				alert(e);
			}
		});
	})
	$('#fileupload_bt').bind('change', function() {
		var fileinput = this;
		var files = this.files;
		for (var i = 0; i < files.length; i++) {
			var name=files[i].name.split('.')[0];
			var type=files[i].name.split('.')[1];
			var a_file = $("<a href='#'>" + files[i].name+ "</a>");
			$(a_file).attr('id',"file"+name);
			var a_delete=$("<a href='#' style='float:right;'>删除</a><br>");
			$(a_delete[0]).attr("id",name);
			$(a_delete[1]).attr("id","br"+name);
			$(a_delete[0]).bind('click',function(){
				var id=$(this).attr('id');
				$("#file"+id).remove();
				$(this).remove();
				$("#br"+id).remove();
				
			})
			a_file.appendTo("#filelist_fs");
			a_delete.appendTo("#filelist_fs");
		}
	})
	
	
</script>