function a(){
	var aVar =1;
	function b(){
		var bVar =2;
		function c(){
			var cVar =3;

		}
		c();
	}
	b();
}
a();