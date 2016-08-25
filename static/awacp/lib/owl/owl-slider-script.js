$(document).ready(function(){
	$("#owl-demo").owlCarousel({
		items : 4
	});
	$('.link').on('click', function(event){
		var $this = $(this);
		if($this.hasClass('clicked')){
			$this.removeAttr('style').removeClass('clicked');
		} 
		else{
			$this.css('background','none').addClass('clicked');
		}
	});
});