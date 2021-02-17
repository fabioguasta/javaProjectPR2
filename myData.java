import java.util.ArrayList;
import java.util.List; 

class myData <E> implements Data<E>{
    /**
     * RI:  el != null && likes >= 0
     * 
     * AF: <el, likes>
     */

    private E el;
    private int numLikes;
    private List<String> friendsLikes;

    public myData(E element)
    {
        this.el = element;
        this.numLikes = 0;
        this.friendsLikes = new ArrayList<String>();
    }

    // Cambia il valore di el
    /**
     * 
     * @param el != null
     * @throws NullPointerException
     * @effects post(this.El) = el
     * 
     */
    public void updateData(E el)throws NullPointerException
    {
        if(el==null) throw new NullPointerException();
        
        this.el=el;
    }

    // Prende il valore di element
    /**
     * 
     * @return copy(this.El)
     */
    public E getData()
    {
        return this.el;
    }

    // Stampa il valore di element
    /**
     * @effects Stampa il valore <El,likes>
     */
    public void Display()
    {
        System.out.println();
        System.out.println(this.el.toString());
        System.out.println(this.numLikes + " ♥");
        System.out.println("------------------");
        
    }

    //restituisce il numero di likes
    /**
     * 
     * @return this.likes
     */
    public int getLikes()
    {
        return this.numLikes;
    }

    //imposta i like ad un valore dato in input
    /**
     * 
     * @effects post(this.likes) = pre(this.likes) + 1
     */
    public void insertLike(String friend) throws NullPointerException, DuplicateLikeException
    {
        if(this.friendsLikes.contains(friend)) throw new DuplicateLikeException();

        this.numLikes++;
        this.friendsLikes.add(friend);
    }

    public Data<E> cloneData() {
        return (Data<E>) new myData<E>(this.getData());
    }
}