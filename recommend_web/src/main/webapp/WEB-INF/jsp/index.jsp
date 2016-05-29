<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored ="false" %>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Recommend System</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="/backend/static/bootstrap-3.3.5-dist/css/bootstrap.min.css">

    <!-- Custom styles for this template -->
    <link href="http://v3.bootcss.com/examples/dashboard/dashboard.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="http://v3.bootcss.com/assets/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="/backend/static/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
    <!-- Just to make our placeholder images work. Don't actually copy the next line! 
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    
    
    <script src="http://v3.bootcss.com/assets/js/ie10-viewport-bug-workaround.js"></script>
    

    
  </head>

  <body>







    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">Recommend System</a>
        </div>
        
        
        <div id="navbar" class="navbar-collapse collapse">

        </div>
      </div>
    </nav>
    
    
    
    
    
    
    
    
    
    

    <div class="container-fluid">
      <div class="row">
      
      
      
      
      
      
      
      
      
      <!-- side bar -->
        <div class="col-sm-3 col-md-2 sidebar">
          <ul class="nav nav-sidebar">
            <li class="active"><a href="#">推荐列表</a></li><br>            
          </ul>
         
        </div>
   
   
   
   
   
   
   
   
   		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        
      
          <br><br>
          

          
          <div role="tabpanel" class="tab-pane" id="tab2" >
          
          
		  <br>
		  
		 <span>用户ID：</span>
         <input id="user_id" />
         &nbsp; &nbsp; &nbsp; &nbsp;
         <span>数据源：</span>
         <input id="source" />
          &nbsp; &nbsp; &nbsp; &nbsp;
         <span>最大数目：</span>
         <input id="num" />
         

          
          <button  id="recommend_submit" class="btn btn-primary">获取推荐</button>
         
          <br><br>
          
          
                <!--  
          <div class="progress">
  			<div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
   			60% 
  			</div>
		  </div>
		  -->
		  
		  
  		  
  		  <br><br>
          
          
          <div id="recommend" >

          </div>

         </div>  <!-- tab2 -->


          
          
         </div>
         
 
 
 
 
 
 
 
 
      </div>
    </div>
    
    <script type="text/javascript">
  
    
    $("#recommend_submit").bind("click",function (){
    	
    	var user_id=$("#user_id").val();
    	var source=$("#source").val();
    	var num=$("#num").val();
    	
    	//alert(user_id+" "+source+" "+num);
    	
    	/*
    	@RequestParam("userid") String userId,
		@RequestParam("numberOfResults") Integer numberOfResults,
		@RequestParam("sourceTypeId") String sourceTypeId)
		*/
		
    	var param={
    			'userid':user_id,
    			'numberOfResults':num,
    			'sourceTypeId':source
    	}
    
    	$.getJSON("/backend/recommend",param,function (json){
			//alert(json);
			
			
			json=$.parseJSON(json)
			
			var text="<table class=\"table table-striped\"><tr> <td>image</td> <td>ISBN</td> <td>book_Title</td> <td>book_Author</td> <td>year_Of_Publication</td> <td>publisher</td> <td>explain</td>  </tr>";
			
			var recommendedItems=json.recommendedItems;
			
			//alert(recommendedItems);
			
			
			
			
			for(var item in recommendedItems ){
				
				//alert(item);
				//alert(recommendedItems[item]);
				
				item=recommendedItems[item]
				
				var item_text="<tr>";
				//item=$.parseJSON(item)
				
				var ISBN=item.itemid;
				var book_Title=item.book_Title;
				var book_Author=item.book_Author;
				var year_Of_Publication=item.year_Of_Publication;
				var publisher=item.publisher;
				var explain=item.explain;
				var image=item.image_URL_S;
				
				item_text+=("<td><img src=\""+image+"\"/></td>"+"<td>"+ISBN+"</td>"+"<td>"+book_Title+"</td>"+"<td>"+book_Author+"</td>"+"<td>"+year_Of_Publication+"</td>"+"<td>"+publisher+"</td>"+"<td>"+explain+"</td>")
				
				text+=item_text;
				
				
			}
			
			text+="</table>";
			
			$("#recommend").html(text);
			
			
			
			
			
			
		});
    	
    
    	
    		
    });
    
    
    </script>

  </body>
</html>


