import java.util.*;

public class Board<E extends Data<?>> implements DataBoard<E> {

    /**
     * 
     * Overview:    contenitore di oggetti generici che estendono il tipo di dato Data. Ogni
     *              dato presente nella bacheca ha associato la categoria del dato.
     *              
     *              amiciAll contiene tutti gli amici
     *              categories = elements.values()
     * 
     * AF:          <password, { el_0, ..., el_dim }, dim> con
     *                  forall i = 0, ..., dim | 
     *                      el_i = <categories.get(i).getCategoryName(), categories.get(i).getData, categories.get(i).getFriends()>
     *                      categories.get(i).categoryName != null    
     * 
     * IR:          password != null && dim = categories.size() &&
     *              ( forall i = 1, ..., dim  |
     *                      categories.get(i).categoryName != null 
     *                      && ( forall j = 0, ..., dim | categories.get(i).getCategoryName() != categories.get(j).categoryName() ) )
     *                      && ( forall k, l = 0, ..., dim | k != l => categories.get(i).getFriend(k) != categories.get(i).getFriend(l) )
     *                      && ( forall m, n = 0, ..., dim | m != n => categories.get(i).getData(m) != categories.get(i).getData(n) ) )
     *                      
     */

    private HashMap<String, Category<E>> elements;      //associazione nome categoria ed oggetto Category
    private int dim;
    private String password;
    private ArrayList<String> amiciAll= new ArrayList<String>();

    //costruttore
    public Board(String password)
    {
        this.password = password;
        this.dim = 0;
        this.elements = new HashMap<String, Category<E>>();
    }

    
     //Crea l’identità una categoria di dati

    public void createCategory(String Category, String passw) throws NullPointerException, InvalidPasswordException, ExistingCategoryException {
        if(Category == null) throw new NullPointerException();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(this.elements.containsKey(Category)) throw new ExistingCategoryException();

        elements.put(Category, new Category<E>(Category));
        if(this.elements.containsKey(Category)) dim++;
    }
    
    // Rimuove l’identità una categoria di dati
    
    public void removeCategory(String Category, String passw) throws InvalidCategoryExcetpion, InvalidPasswordException, NullPointerException {
        if(Category == null) throw new NullPointerException();
        if(!this.elements.containsKey(Category)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        elements.remove(Category);
    }

    // Aggiunge un amico ad una categoria di dati
    
    public void addFriend(String Category, String passw, String friend) throws InvalidCategoryExcetpion, ExistingFriendException, InvalidPasswordException, NullPointerException {
        if(Category == null) throw new NullPointerException();
        if(!this.elements.containsKey(Category)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        this.elements.get(Category).addFriend(friend);
        addFriendGenerale(friend);

    }

 
   //rimuove un amico da una categoria 
    public void removeFriend(String Category, String passw, String friend) throws InvalidCategoryExcetpion, InvalidPasswordException, InvalidFriendException, NullPointerException  {
        if(!this.elements.containsKey(Category)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(friend == null || Category == null) throw new NullPointerException();
        if(!this.elements.get(Category).removeFriend(friend)) throw new InvalidFriendException();
    }
    
    // Inserisce un dato in bacheca
    // se vengono rispettati i controlli di identità

    public boolean put(String passw, E dato, String categoria) throws DuplicateDataException, InvalidCategoryExcetpion, DuplicateDataException, InvalidPasswordException, NullPointerException {
        if(!this.elements.containsKey(categoria)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(dato == null || categoria == null) throw new NullPointerException();

        this.elements.get(categoria).addData(dato);

        return this.elements.get(categoria).contains(dato);
    }
    
    // Ottiene una copia del del dato in bacheca
    // se vengono rispettati i controlli di identità
    
    public E get(String passw, E dato) throws InvalidDataException, InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        
        Collection<Category<E>> categories = this.elements.values();

        for(Category<E> category: categories) {
            if(category.contains(dato))
                return (E) dato.cloneData();
        }

        throw new InvalidDataException();
    }

    // Rimuove il dato dalla bacheca
    // se vengono rispettati i controlli di identità
    
    public E remove(String passw, E dato) throws InvalidDataException {
        boolean found = false;

        for(Category<E> category : this.elements.values()) {
            if(category.removeDataIfExists(dato)) found = true;
        }

        if(found) return dato;

        throw new InvalidDataException();
    }

    // Crea la lista dei dati in bacheca su una determinata categoria
    // se vengono rispettati i controlli di identità
    
    public List<E> getDataCategory(String passw, String Category) throws InvalidCategoryExcetpion, InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(Category == null) throw new NullPointerException();
        if(!this.elements.containsKey(Category)) throw new InvalidCategoryExcetpion();
        
        return this.elements.get(Category).getData();
    }
    
    // restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca ordinati rispetto al numero di like.
    
    public Iterator<E> getIterator(String passw) throws InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        SortedSet<E> bacheca = new TreeSet<E>(new SortByLikes());

        for(Category<E> category : this.elements.values()) {
            if(category.getData() != null)
                bacheca.addAll(category.getData());
        }
                
        return Collections.unmodifiableSortedSet(bacheca).iterator();
    }

    // Aggiunge un like a un dato
    
    public void insertLike(String friend, E dato) throws InvalidFriendException, InvalidDataException, DuplicateLikeException {

        if(friend == null || dato == null) throw new NullPointerException();

        boolean foundFriend = false, foundPost = false;

        for(Category<E> category : this.elements.values()) {
            if(category.contains(friend)) {
                foundFriend = true;
                if(category.contains(dato)){
                    foundPost = true;
                    if(category.contains(dato) && category.contains(friend)) {
                        dato.insertLike(friend);
                        return;
                    }
                }
            }
            
        }

        if(!foundFriend) throw new InvalidFriendException();
        if(!foundPost) throw new InvalidDataException();
    }

    //Aggiunge un amico alla lista generale 
    public void addFriendGenerale(String friend)
    {
        if(!amiciAll.contains(friend))
            amiciAll.add(friend);
    }
    //rimuove un amico dalla lista generale
    public void removeFriendGenerale(String friend)
    {
        if(amiciAll.contains(friend))
            amiciAll.remove(friend);
    }

    // Legge un dato condiviso
    // restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca condivisi.
    
    public Iterator<E> getFriendIterator(String friend) throws InvalidFriendException {
        if(friend == null) throw new NullPointerException();
        
        Set<E> bacheca = new HashSet<E>();

        for(Category<E> category : this.elements.values())
            if(category.getFriends().contains(friend)) bacheca.addAll(category.getData());

        if(bacheca.isEmpty()) throw new InvalidFriendException();

        return Collections.unmodifiableSet(bacheca).iterator();
    }

    /**
     * 
     * @return dim
     */
    public int numCategories() {
        return this.dim;
    }

    /**
     * 
     * @param category t.c. this.elements.containsKey(category)
     * @throws InvalidCategoryExcetpion if !this.elements.containsKey(category)
     * @return this.elements.get(category).numFriends()
     */
    public int numFriends(String category) throws InvalidCategoryExcetpion {
        if(!this.elements.containsKey(category)) throw new InvalidCategoryExcetpion();
        return this.elements.get(category).numFriends();
    }

    /**
     * 
     * @param category t.c. this.elements.containsKey(category)
     * @throws InvalidCategoryExcetpion if !this.elements.containsKey(category)
     * @return this.elements.get(category).numData()
     */
    public int numData(String category) throws InvalidCategoryExcetpion {
        if(!this.elements.containsKey(category)) throw new InvalidCategoryExcetpion();
        return this.elements.get(category).numData();
    }

}