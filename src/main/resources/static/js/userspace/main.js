
// DOM 加载完再执行
$(function() {
	var avatarApi;
	
	// 获取编辑用户头像的界面
	$(".blog-content-container").on("click",".blog-edit-avatar", function () { 
		avatarApi = "/u/"+$(this).attr("userName")+"/avatar";
		$.ajax({ 
			 url: avatarApi, 
			 success: function(data){
				 $("#avatarFormContainer").html(data);
		     },
		     error : function() {
		    	 alert("error!");
		     }
		 });
	});
	
	// 提交用户头像的图片数据
	$("#submitEditAvatar").on("click", function () {  
	    var formData = new FormData();
	    formData.append("file",$("#file")[0].files[0]);
	    // 获取 CSRF Token 
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		console.log(avatarApi);
    	// 保存头像更改到数据库
		$.ajax({ 
			 url: avatarApi, 
			 type: 'POST',
			 data:formData,
			 processData:false,
			 contentType:false,
			 beforeSend: function(request) {
                 request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token 
             },
			 success: function(data){
				 if (data.success) {
					// 成功后，置换头像图片
					 location.reload();
					 alert(data.message);
				 } else {
					 alert("error!"+data.message);
				 }
				 
		     },
		     error : function() {
		    	 alert("error!");
		     }
		 });
	}); 
	
});