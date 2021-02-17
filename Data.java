public interface Data<E> {
    
    /**
     * Overview:    Contenitore di un parametro generico con associate descrizione, autore e insieme degli amici
     *              che hanno inserito un like
     * 
     * TE:          <element, friends, numLikes>, 
     *              element != null &&
     *              friends = { friend_1, friend_2, ..., friends_getLikes() } &&
     *              likes = #friends
     */

    // Mofica il valore di el
    /**
     * 
     * @param el, el != null
     * @throws NullPointerException if el = null
     * @modifies this.element
     * @effects post(this.element) = el
     * 
     */
    public void updateData(E el);

    // Restituisce il valore dell'elemento
    /**
     * 
     * @return element
     */
    public E getData();

    // Restituisce il numero di like associati al dato
    /**
     * 
     * @return this.likes
     */
    public int getLikes();

    // Stampa il valore di element
    /**
     * 
     * @effects stampa in formato stringa element
     */
    public void Display();

    /**
     * @param friend t.c. forall i = 1, ..., getLikes() | friend_i != friend
     * @throws DuplicateLikeException if exist i = 1, ..., getLikes() | friend_i = friend
     * @modifies this.likes && this.friends
     * @effects post(this.likes) = this.likes + 1 && post(this.friends) = pre(this.friends) U friend
     */
    
    public void insertLike(String friend) throws NullPointerException, DuplicateLikeException;

    /**
     * 
     * @return una deep copy di this
     */
    
    public Data<E> cloneData();
}
