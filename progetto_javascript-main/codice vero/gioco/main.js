
var script = document.createElement('script');
script.src = 'https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js'; 
document.getElementsByTagName('head')[0].appendChild(script);

script = document.createElement('script');
script.src = 'Utilities.js'; 
document.getElementsByTagName('head')[0].appendChild(script);

script = document.createElement('script');
script.src = 'Giocatore.js'; 
document.getElementsByTagName('head')[0].appendChild(script);

script = document.createElement('script');
script.src = 'Configuratore.js'; 
document.getElementsByTagName('head')[0].appendChild(script);

script = document.createElement('script');
script.src = 'map.js'; 
document.getElementsByTagName('head')[0].appendChild(script);

script = document.createElement('script');
script.src = 'PartitaController.js'; 
document.getElementsByTagName('head')[0].appendChild(script);
//parte di trasferimento dati tra le pagine

var numeroGioc=3;
var giocatori=new Array(numeroGioc);
var carte_per_gioc=14;
/*
$(document).delegate("#pag2_container #secondapaginabutton", "click", function(){
	alert("hai cliccato sulla seconda pagina");
});

$(document).delegate("#home_container button", "click", function(){
	console.log("ciao");
	$.ajax({
			url: "pag2.html", 
			success: function(result){
				$("body").html(result);
				x++;
			}
	});
});
*/
$(document).ready(function(){


    $("button").click(function(event){
        id=event.target.id;
        numeroGioc=2+parseInt(id.substring(4,5));
		if(numeroGioc==3){
			carte_per_gioc=14;
		}
		if(numeroGioc==4){
			carte_per_gioc=10;
		}
		if(numeroGioc==5){
			carte_per_gioc=8;
		}
		if(numeroGioc==6){
			carte_per_gioc=7;
		}
		giocatori=new Array(numeroGioc);
        $.ajax({
			url: "configurazione02.html", 
			success: function(result){
				$("body").html(result);
				
			}
        });
        
	});
    


});

//input dei giocatori
//FINITO
//FINITO
//FINITO
var count=1;
var i=0;
$(document).delegate("#next", "click", function(){
	var errore;
	count++;
	$('h1').html('Impostare Giocatore '+count)
	var giocatore=new Giocatore();
	giocatore.numeroArmateIniziali=35-(5*(numeroGioc-3))
	if(!$("#nomeX").val()){
		errore=true;
	}
	else{
		giocatore.setNome($("#nomeX").val());
	}
	if(!$("#cognomeX").val()){
		errore=true;
	}
	else{
		giocatore.setCognome($("#cognomeX").val());
	}
	if(!$("#nickX").val()){
		errore=true;
	}
	else{
		giocatore.setNickName($("#nickX").val());
	}
	if(!$("#colorX").val()){
		errore=true;
	}
	else{
		giocatore.setColoreArmate($("#colorX").val());
	}
		giocatori[i]=giocatore;
	if(!errore){
		i++;
	}
	if(i===numeroGioc){
		$.ajax({
			url: "configurazione03.html", 
			success: function(result){
				$("body").html(result);
				
			}
		}); 

		giocatori=Configuratore.DistribuisciCarteTerritorio(giocatori, territori, carte_per_gioc);
	}
});
//FINITO
//FINITO
//FINITO

//rivela giocatori
$(document).delegate("#bubuSettete", "click", function(){
	var constructor=" ";

	for(var i=0; i<=numeroGioc-1;i++){
		constructor+='<div class="div1"><h1>GIOCATORE '+(i+1)+'</h1><button class="buttondiv bb" id="dati'+i+'">visualizza dati giocatore</button><br><button class="carte" id="carte'+i+'">prendi carte</button><br><button class="buttondiv" id="visualizza'+i+'">visualizza carte</button></div>';
	}
	
	$("p").html(constructor);

	$(".carte").click(function(e){
		var p=e.target.id;
		p=parseInt(p.substring(5,6));
		var prendiC="";
		var c=giocatori[p];
		c.carteTerritori.forEach(element => {
			prendiC+=element.nome+", ";

		});
		$("#visualizza"+p).html(prendiC);

	});

	$(".buttondiv").click(function(e){
		var p=e.target.id;
		p=parseInt(p.substring(4,5));
		var dati="Nome: ";
		var c=giocatori[p];
		
		dati+=c.nome+", cognome:";
		dati+=c.cognome+", nickname:";
		dati+=c.nickName+", colore: ";
		dati+=c.coloreArmate;

		$("#dati"+p).html(dati);

	});
});
 
//passaggio a mappa partita
$(document).delegate("#gioca", "click", function(){

	$.ajax({
		url: "mappa.html", 
		success: function(result){
			$("body").html(result);
			
		}
	});



});

//start partita

var map;
var fine;
var aree;
var riserva;
var riservaTitolo;
var nomeGiocatore;
var infoFase;
var bottoneTurno;
$(document).delegate("#start", "click", function(){

	
	PartitaController.IniziaPartita(giocatori);

});
