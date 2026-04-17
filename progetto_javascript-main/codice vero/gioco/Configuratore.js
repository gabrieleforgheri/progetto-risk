class Configuratore{
    constructor(){

    }
    
    static Configura(){
        //contiene scelta numero giocatori e specifiche per giocatore
    }

    static DistribuisciCarteTerritorio(giocatori, carteTerritorio, numeroCarteGiocatore){
        carteTerritorio= RisikoHelper.mescola(carteTerritorio);
        giocatori.forEach(element => {
            for(var i=numeroCarteGiocatore-1; i>=0;i--){
                element.addCarteTerritorio(carteTerritorio[i]);
                const index = carteTerritorio.indexOf(carteTerritorio[i]);
                carteTerritorio.splice(index, 1);
            }
        });
        
        if(carteTerritorio.length>0){
            var i=0;
            carteTerritorio.forEach(element=>{
                giocatori[i].addCarteTerritorio(element);
                i++;
            });

        }
        giocatori.forEach(gioc=>{
            gioc.carteTerritori.forEach(terr=>{
                if(!carteTerritorio.includes(terr)){
                    carteTerritorio.push(terr);
                }
            });
        });
        return giocatori;
    }
    static MostraCarte(){

    }
    static settaAree(){
        jQuery.each(aree, function (i,area) {
            territori.forEach(function (country) {
                if (country.nome == area.id) {
                    country.numeroArmate+=1;
                    area.style.fill = country.colore;
                    area.nextElementSibling.textContent = country.numeroArmate;
                   
                }
            });
        });
    }
    static settaGiocatori(){
        var c="";
        giocatori.forEach(function(gioc){
            c+='<div class="utente" style="background-color:'+gioc.coloreArmate+';"><p class="utente-p">'+gioc.nome+'</p><p class="utente-p">'+gioc.cognome+'</p><p class="utente-p">'+gioc.nickName+'</p></div>';
        });
        $('#box-utenti').html(c);
    }
}

