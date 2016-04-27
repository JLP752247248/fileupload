<%@ page language="java" contentType="text/html; charset=UTF-8"
	 pageEncoding="UTF-8"%>
	<script
	src="<%=request.getContextPath()%>/jsp/third-party/jquery-1.10.2.js"></script>
	<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/jsp/third-party/webuploader/webuploader.css">

<script type="text/javascript" charset="utf-8"
	src="<%=request.getContextPath()%>/jsp/third-party/webuploader/webuploader.js">
</script>

<body>
	<!-- 上传wj -->
	<div id="uploader" class="wu-example">
    <!--用来存放文件信息-->
    <div class="btns">
        <div id="picker">选择文件</div>
        <label id="ctlBtn" class="btn">开始上传</label>
    </div>
    <div id="thelist" class="uploader-list"></div>
	</div>


</body>


<script type="text/javascript">
//var ue = UE.getEditor('editor');
	// 文件上传
	
	
	jQuery(function() {
		var $ = jQuery,
        $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader;

    uploader = WebUploader.create({

        // 不压缩image
        resize: false,

        // swf文件路径
        swf: "http://localhost:8080/fileuploader/jsp/third-party/webuploader/Uploader.swf",

        // 文件接收服务端。
        server: 'http://localhost:8080/fileuploader/UploadServlet',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
       	// chunkSize :1024*1024,
        chunked: true,
        threads:3
        
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
    	//file.currentPercent=0.5;
        $list.append( '<div id="' + file.id + '" class="item">' +
            '<h4 class="info">' + file.name + '</h4>' +
            '<p class="state">等待上传...</p>' +
        '</div>' );
    });
    
 	// 当有文件添加进来的时候
    uploader.on( 'uploadBeforeSend', function( object,data ) {
        $.extend(data,{dividNum:object.chunks,
        	chunkSize:object.transport.options.chunkSize,
        	currentPercent:object.file.currentPercent});
       
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id );
            $percent = $li.find('.progress .progress-bar');
            
        // 避免重复创建
        if ( !$percent.length ) {
        	
            $percent = $('<div class="progress progress-striped active" >' +
              '<div class="progress-bar progress" role="progressbar" >' +
              '</div>' +
            '</div>').appendTo( $li ).find('.progress-bar');
            
        }
        $li.find('p.state').text('上传中');
        var percent=(percentage*100).toFixed(2);
        	
        var $text=$li.find('.progress .progress-bar .percent');
        
        if($text)
       		{	
        		$text.remove();
       		}
        $text=$('<div class="percent progress"></div>');
        $text.text(percent+'%')
        $percent.css('width',percentage * 100 + '%');
        //console.log(percentage*100);
        $text.appendTo($percent);
        
    });

    uploader.on( 'uploadSuccess', function( file ) {
    	
        $( '#'+file.id ).find('p.state').text('已上传');
    });

    uploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
    });

    uploader.on( 'uploadComplete', function( file ) {
       // $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }

        if ( state === 'uploading' ) {
            $btn.text('暂停上传');
        } else {
            $btn.text('开始上传');
        }
    });

    $btn.on( 'click', function() {
        if ( state === 'uploading' ) {
            uploader.stop(true);
        } else {
            uploader.upload();
        }
    });
	});
	

	
</script>