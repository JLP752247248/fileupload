<%@ page language="java" contentType="text/html; charset=UTF-8"
	 pageEncoding="UTF-8"%>

<script type="text/javascript" charset="utf-8"
	src="<%=request.getContextPath()%>/jsp/test.js">
</script>

<body>
	<form action="/fileuploader/UploadServlet" method="POST" ENCTYPE="multipart/form-data">
		<input type="file" id="file1" name="file1"/>
		<input type="text" id="maxsize"/>
		<input type="submit" value="subfile"/>
	</form>
</body>


