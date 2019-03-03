// DOM 加载完再执行
$(function() {
 	$("#uploadImage").click(function() {
 		var avatarApi = "/u/"+$(this).attr("userName")+"/blogs/photo";
	    // 获取 CSRF Token 
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		// 保存图片更改到数据库
		$.ajax({ 
			 url: avatarApi, 
			 type: 'POST',
			 cache: false,
			 data: new FormData($('#uploadformid')[0]),
			 processData:false,
			 contentType:false,
			 beforeSend: function(request) {
                 request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
             },
			 success: function(data){
		    	var mdcontent=$("#md").val();
		    	 $("#md").val(mdcontent + "\n![]("+data.body +") \n");
 
	         }
		}).done(function(res) {
			$('#file').val('');
		}).fail(function(res) {});
 	})
 
 	// 发布博客
 	$("#submitBlog").click(function() {
 
		// 获取 CSRF Token 
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
		    url: '/u/'+ $(this).attr("userName") + '/blogs/edit',
		    type: 'POST',
			contentType: "application/json; charset=utf-8",
		    data:JSON.stringify({"id":Number($('#id').val()), 
		    	"title": $('#title').val(), 
		    	"summary": $('#summary').val(), 
		    	"content": $('#md').val(),
		    	"tags":$('.form-control-tag').val(),
		    	"catalog":{"id":$('#catalogSelect').val()}}),
			beforeSend: function(request) {
			    request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
			},
			 success: function(data){
				 if (data.success) {
					// 成功后，重定向
					 window.location = data.body;
				 } else {
					 alert("error!"+data.message);
				 }
				 
		     },
		     error : function() {
		    	 alert.error("error!");
		     }
		})
 	})
 	
});