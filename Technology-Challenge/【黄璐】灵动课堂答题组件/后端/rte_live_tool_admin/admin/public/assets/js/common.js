function closeDialog(){
		
    var modalObj= $("body").attr("data-target");
    modalObj= $("#"+modalObj);
	modalObj.modal('hide');
	 
 }

function showDialog(title,content,time){
	  $.teninedialog({
		  title:title,
          content:content,
          dialogShow:function(){
        	  if(time != null && !isNaN(time)){
        		  setTimeout('closeDialog()',time);
        	  }
          }

      });
}
