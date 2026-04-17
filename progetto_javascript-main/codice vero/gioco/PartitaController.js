var faseiniz=numeroGioc;
var giocatoreCorrente;
class PartitaController{
    constructor(){
        this.numeroArmatePerGiocatore;
    }
    
    static IniziaPartita(){
        //inzio pre partita
        //inizializzo variabili
        $('#start').css("display","none");
        $('#fine').click(function(){
            $(location).prop('href', 'http://www.example.com')
        })
        bottoneTurno=$('#end');
        bottoneTurno.css("display","none");
        bottoneTurno.click(Turno.buttonHandleClick);
        map=$('svg');
	    fine=$('#fine');
	    aree=$('.area');
        riserva=$('#reserve');
        riservaTitolo=$('#reserve-title');
        nomeGiocatore=$('#player-name');
        infoFase=$('#turn-info-message');
        //do ad ogni territorio il metodo per i click;
	    aree.click(Turno.handleClick);
        Turno.fase="Rinforzo";
        Partita.turnoGioc=0;
        giocatori.forEach(function(gioc){
            gioc.numeroArmateIniziali=gioc.numeroArmateIniziali-gioc.carteTerritori.length;
            //da sistemare
            Turno.numeroArmateRinforzo=gioc.numeroArmateIniziali;
        })

        //traformare le carte territorio in territoriConquistati;

        giocatori.forEach(function(gioc){
            gioc.carteTerritori.forEach(function(terr){
                gioc.addTerritoriConquistati(terr);
            })
            gioc.removeAllCarteTerritorio();
        });

        //mostro a schermo le info per il panel
        giocatoreCorrente=giocatori[0];
        riserva.html(giocatori[0].numeroArmateIniziali);
        nomeGiocatore.html(giocatori[0].nickName);
        infoFase.html("clica su un territorio in tuo possesso per aumentare di 1 le armate di quel territorio")
        Configuratore.settaGiocatori();
        Configuratore.settaAree();
    }
    static TerminaPartita(){

    }

}
class Partita{
    constructor(numeroGiocatori){
        this.vittoria=false;
        this.numeroGiocatori=numeroGiocatori;
        this.giocatoreVincitore;
        this.turnoGioc=0;
    }
    static CambiaTurno(){
        
        Partita.turnoGioc++;
        faseiniz--;
        if(Partita.turnoGioc>2){
            Partita.turnoGioc=0;
        }
        giocatoreCorrente=giocatori[Partita.turnoGioc]
        if(faseiniz<=0){
            giocatoreCorrente.numeroArmateIniziali=Turno.CalcoloAramate(giocatoreCorrente,continenti);
        }
        
        nomeGiocatore.html(giocatoreCorrente.nickName);
        infoFase.html("clica su un territorio in tuo possesso per aumentare di 1 le armate di quel territorio")
        riserva.html(giocatoreCorrente.numeroArmateIniziali);
        Turno.numeroArmateRinforzo=giocatoreCorrente.numeroArmateIniziali;
    }
    static Vittoria(){

    }
}

class Turno{
    constructor(numeroArmateRinforzo,numeroArmateSpostate, territorioDaSpostare, territorioDaAumentare){
        this.territorioDaSpostare=territorioDaSpostare;
        this.territorioDaAumentare=territorioDaAumentare;
        this.numeroArmateRinforzo=numeroArmateRinforzo;
        this.numeroArmateSpostate=numeroArmateSpostate;
        this.fase="";
        this.inizio;
    }
    static CalcoloAramate(giocatore, continenti){
        Turno.numeroArmateRinforzo=0;

        Turno.numeroArmateRinforzo+=Math.ceil(giocatore.territoriConquistati.length / 3);
        continenti.forEach(element=>{
            if(giocatore.territoriConquistati.includes(element.territori)){
                Turno.numeroArmateRinforzo+=element.numeroArmateSupplementari;
            }
        });
        return Turno.numeroArmateRinforzo;

    }
    static EseguiSpostamento(e){
        if(Turno.targerPrecedente){
            Turno.targerPrecedente.classList.remove('evidenzia')
        }
        territori.forEach(function(territorio){
            if(e.target.id===territorio.nome){
                e.target.classList.add('evidenzia');
                Turno.targerPrecedente=e.target;
                if(Turno.territorioPrecedente){
                    if(Turno.territorioPrecedente.nome!=territorio.nome && Turno.territorioPrecedente.propretario==territorio.propretario){
                        Attacco.territorioPrecedente.confinaCon.forEach(function(vicino){
                            if(vicino==territorio.nome&&vicino.propretario!=territorio.propretario && Turno.territorioPrecedente.numeroArmate>1){
                                
                                var carriDaSpostare=parseInt(prompt("inserire un numero tra: " +0+ " e "+(Turno.territorioPrecedente.numeroArmate-1)+" per determinare i carri armati da spostare." ));
                                var territorioAggiungere = $("#"+territorio.nome);
                                var territorioTogliere = $("#"+Turno.territorioPrecedente.nome); 

                                territorioAggiungere.next().html(territorio.numeroArmate+carriDaSpostare);
                                territorio.numeroArmate = territorio.numeroArmate+carriDaSpostare;
                                territorioTogliere.next().html(Turno.territorioPrecedente.numeroArmate - carriDaSpostare);
                                Turno.territorioPrecedente.numeroArmate = Turno.territorioPrecedente.numeroArmate - carriDaSpostare;
                            }
                        });
                    }
                }
                Turno.territorioPrecedente=territorio;
            }
        });
    }
    static EseguiRinforzo(giocatore){
        
        this.CalcoloAramate(giocatore, continenti);

        
    }
    //esegue il piazzamento di una singola armata situata su event e utilizza 
    //la classe per capire se si è nel rinforzo o nello spostamento
    static EseguiPiazzamento(e){
        //aumento di uno il suo territorio
        territori.forEach(function(territorio){
            
            if(e.target.id==territorio.nome && giocatoreCorrente.nickName==territorio.propretario && Turno.numeroArmateRinforzo>0){
                territorio.numeroArmate+=1;
                Turno.numeroArmateRinforzo-=1;
                riserva.html(Turno.numeroArmateRinforzo);
                e.target.nextElementSibling.textContent = territorio.numeroArmate;
                if(Turno.numeroArmateRinforzo==0){
                    giocatoreCorrente.numeroArmateIniziali=0;
                    riservaTitolo.html("");
                    riserva.html("");
                    Turno.fase="Attacco";
                    bottoneTurno.css("display","block");
                    bottoneTurno.html("Cambio Fase");
                    infoFase.html("scegli un territorio da cui attaccare e uno da bersagliare")
                }
            }
        });
    }

    static handleClick(e){
        if(Turno.fase=="Rinforzo"){
            Turno.EseguiPiazzamento(e);
        }
        else if(Turno.fase=="Attacco"){
            Attacco.Esegui(e);
        }
        else if(Turno.fase=="Spostamento"){
            Turno.EseguiSpostamento(e);
        }
    }
    static buttonHandleClick(e){
        if(Turno.fase=="Attacco"){
            Turno.fase="Spostamento";
            e.target.innerHTML="Fine Turno";

        }
        else if(Turno.fase=="Spostamento"){
            Turno.fase="Rinforzo";
            e.target.style.display="none";
            Partita.CambiaTurno();
        }
    }

}

class Attacco{
    constructor(){
        this.targerPrecedente;
        this.territorioPrecedente;
    }
    static Esegui(e){
        if(Attacco.targerPrecedente){
            Attacco.targerPrecedente.classList.remove('evidenzia')
        }
        territori.forEach(function(territorio){
            if(e.target.id===territorio.nome){
                e.target.classList.add('evidenzia');
                Attacco.targerPrecedente=e.target;
                if(Attacco.territorioPrecedente){
                    if(Attacco.territorioPrecedente.nome!=territorio.nome && Attacco.territorioPrecedente.propretario!=territorio.propretario && Attacco.territorioPrecedente.propretario==giocatoreCorrente.nickName){
                        Attacco.territorioPrecedente.confinaCon.forEach(function(vicino){
                            if(vicino==territorio.nome && Attacco.territorioPrecedente.numeroArmate>0){
                                console.log(vicino);
                                return Battaglia.Esegui(Attacco.territorioPrecedente, territorio, giocatoreCorrente,0);
                            }
                        });
                    }
                }
                Attacco.territorioPrecedente=territorio;
            }
        });
    }
}

class Battaglia{
    constructor(numeroDadiAttaccante,numeroDadiDifensore,
        numeroArmatePerseAttaccante,numeroArmatePerseDifensore,numeroArmateAttaccante,numeroArmateDifensore){

            this.numeroDadiAttaccante=numeroDadiAttaccante
            this.numeroDadiDifensore=numeroDadiDifensore
            this.numeroArmatePerseAttaccante=numeroArmatePerseAttaccante
            this.numeroArmatePerseDifensore=numeroArmatePerseDifensore
            this.numeroArmateAttaccante=numeroArmateAttaccante
            this.numeroArmateDifensore=numeroArmateDifensore
        }
        static Esegui(territorioAttacante, territorioDifensore, playerAttaccante, i){

            
            var difensore = $("#"+territorioDifensore.nome)
            var attaccante = $("#"+territorioAttacante.nome) 
            var playerDifensore;

            //ricerca giocatore avversario (capire come inviare array di giocatori)
            
            giocatori.forEach(function(p){
                if(p.nickName == territorioDifensore.propretario){
                    
                    playerDifensore = p;
                }
            })
            console.log(difensore);
            console.log(territorioDifensore);
            //logica della battaglia:
            while(territorioDifensore.numeroArmate >= 0){
                if(territorioAttacante.numeroArmate == 1){
                    attaccante.next().html(1);
                    difensore.next().html(territorioDifensore.numeroArmate);
                    return;
                }
                if(Dado.Lancia()){
                    territorioAttacante.numeroArmate -= 1; 
                }
                else{
                    territorioDifensore.numeroArmate -= 1;
                }
            }
            console.log("ciao");
            //se giocatore vince:
            if(territorioDifensore.numeroArmate <= 0 ){
                //Remove area from defenders territoriConquistati array
                giocatori.forEach(player => {
                    if(player.nome === territorioDifensore.propretario){
                        let index = player.territoriConquistati.indexOf(territorioDifensore.nome);
                        if (index > -1) {
                            player.territoriConquistati.splice(index, 1);
                        }
                    }
                });
                
                //Swap defender area to attacker and distribute numeroArmate evenly between territoriConquistati
                territorioDifensore.propretario = playerAttaccante.nickName;
                territorioDifensore.colore = playerAttaccante.coloreArmate;
                playerAttaccante.territoriConquistati.push(territorioDifensore);
                
                difensore.css("fill",territorioDifensore.colore);
                var togli=Math.floor(territorioAttacante.numeroArmate / 2);
                difensore.next().html(togli);
                territorioDifensore.numeroArmate = togli;
                attaccante.next().html(territorioAttacante.numeroArmate - togli);
                territorioAttacante.numeroArmate = territorioAttacante.numeroArmate - togli;
                
                //If Defender has no territoriConquistati left they are eliminated
                if(playerDifensore.territoriConquistati.length === 0){
                    playerDifensore = false;
                    let index = giocatori.indexOf(playerDifensore)
                    infoName[index].parentElement.classList.add('defeated');
                }
            }
            
            //Calcualting total numeroArmate for each player
            playerAttaccante.numeroArmate = 0;
            playerDifensore.numeroArmate = 0;
            territori.forEach(c => {
                playerAttaccante.territoriConquistati.forEach(area => {
                    if(area === c.nome){
                        playerAttaccante.numeroArmate += c.numeroArmate
                    }
                })
                playerDifensore.territoriConquistati.forEach(area => {
                    if(area === c.nome){
                        playerDifensore.numeroArmate += c.numeroArmate
                    }
                })
            })
            
            //Display Bonus modal if player controls continente
            if(playerAttaccante.alive){
                continenti.forEach(continente => {
                    if(playerAttaccante.territoriConquistati.containsArray(continente.territoriConquistati)){   
                        let matchedCountry = continente.territoriConquistati.some(a => {
                        return a === territorioDifensore.nome;
                        });
                        if(matchedCountry){

                            //SISTEMARE MARCELLO... dopo aver finito interfaccia utente
                            //SISTEMARE MARCELLO... dopo aver finito interfaccia utente
                            //SISTEMARE MARCELLO... dopo aver finito interfaccia utente

                            bonusModal.style.display = "block";
                            bonusModalPlayer.textContent = player.nome + " controls"
                            bonusModalText.textContent = continente.nome;
                            bonusModalText.style.color = player.colore;
                            bonusModalAmount.textContent = continente.bonus;
                            setTimeout(() => {
                                bonusModal.style.display = "none";
                            }, 2000);
                        }
                    }
                })  
            }
            
            //Win Condition
            /*
            if(playerAttaccante.territoriConquistati.length === 42){
                this.gameOver = true;
                this.win(playerAttaccante);
            }
            */
        }
      
    }

class Dado{
    static Lancia(){
        return (Math.random()*5)+1 > (Math.random()*5)+1
    }
}