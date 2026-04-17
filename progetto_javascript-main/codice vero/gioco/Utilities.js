//questo file contiene le helper function di uso comune
class RisikoHelper{

    static mescola(array) {
        //Ci prendiamo la lunghezza dell'array e partiamo dal fondo!
        var currentIndex = array.length, temporaryValue, randomIndex;
        // Finché ci sono elementi da mescolare, iteriamo l'array
        while (0 !== currentIndex) {
          //Prendiamo un indice a caso dell'array, purché sia compreso tra 0 e la lunghezza dell'array
          randomIndex = Math.floor(Math.random() * currentIndex);
          //Riduciamo di un'unità l'indice corrente
          currentIndex -= 1;
          // Una volta che abbiamo preso l'indice casuale, invertiamo l'elemento che stiamo analizzando alla posizione corrente (currentIndex) con quello alla posizione presa casualmente (randomIndex)
          //Variabile temporanea
          temporaryValue = array[currentIndex];
          //Eseguiamo lo scambio
          array[currentIndex] = array[randomIndex];
          array[randomIndex] = temporaryValue;
        }
        //Torniamo l'array mescolato a fine ciclo
        return array;
      } 
}
