class Giocatore{
    constructor(nome, cognome, nickName, numeroArmateIniziali,numeroArmate, coloreArmate, sopravissuto){
        this.setNome(nome);
        this.cognome=cognome;
        this.nickName=nickName;
        this.numeroArmateIniziali=numeroArmateIniziali;
        this.numeroArmate=numeroArmate;
        this.coloreArmate=coloreArmate;
        this.sopravissuto=sopravissuto;
        this.carteTerritori=new Array();
        this.territoriConquistati=new Array();
        
    }
    addTerritoriConquistati(elem){
        elem.propretario = this.nickName;
        elem.colore=this.coloreArmate;
        this.territoriConquistati.push(elem);
    }
    removeTerritoriConquistati(elem){
        const index = this.territoriConquistati.indexOf(elem);
        this.territoriConquistati.splice(index, 1); 
        
    }
    addCarteTerritorio(elem){
        elem.propretario = this.nickName;
        elem.colore=this.coloreArmate;
        this.carteTerritori.push(elem)
    }
    removeCarteTerritorio(elem){
        const index = this.territoriConquistati.indexOf(elem);
        this.carteTerritori.splice(index, 1);
    }
    removeAllCarteTerritorio(){
        this.carteTerritori=new Array();
    }



    //set e get
    setNome(nome){
        this.nome=nome;
    }
    setCognome(cognome){
        this.cognome=cognome;
    }
    setNickName(nickName){
        this.nickName=nickName;
    }
    setNumeroArmateIniziali(numeroArmateIniziali){
        this.numeroArmateIniziali=numeroArmateIniziali;
    }
    setNumeroArmate(numeroArmate){
        this.numeroArmate=numeroArmate;
    }
    setColoreArmate(coloreArmate){
        this.coloreArmate=coloreArmate;
    }
    setSopravissuto(sopravissuto){
        this.sopravissuto=sopravissuto;
    }

    getNome(){
        return this.nome;
    }
    getCognome(){
        return this.cognome;
    }
    getNickName(){
        return this.nickName;
    }
    getNumeroArmateIniziali(){
        return this.numeroArmateIniziali;
    }
    getNumeroArmate(){
        return this.numeroArmate;
    }
    getColoreArmate(){
        return this.coloreArmate;
    }
    getSopravissuto(){
        return this.sopravissuto;
    }
    getNumeroCarteTerritori(){
        return this.carteTerritori.length;
    }
}