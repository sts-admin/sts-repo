var s3 = $("#ranged-value").freshslider({
	range: true,
	step:1,
	value:[0, 5000],
	onchange:function(low, high){
		console.log(low, high);
	}
});