var bottoniC1=document.getElementsByClassName("bottoniConf1");
var ripetereGioc;
for(var i=0; i<bottoniC1.length;i++){
    bottoniC1[i].onclick=quantiGiocatori;
}


function quantiGiocatori(event){
    console.log("eccomi")
    var c=event.target.getAttribute("id");
    c=parseInt(c.substring(4,5));
    location.href="Configurazione02.html";
    ripetereGioc=c;
}




