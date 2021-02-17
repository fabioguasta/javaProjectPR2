import java.util.*;

public class Board2<E extends Data<?>> implements DataBoard<E> {

    /**
     * 
     * Overview:    contenitore di oggetti generici che estendono il tipo di dato Data. Ogni
     *              dato presente nella bacheca ha associato la categoria del dato.
     * 
     *              amiciAll contiene tutti gli amici
     *              categories = dataset.keySet() = friends.keySet()
     * 
     * AF:          <password, { el_0, ..., el_dim }, dim> con
     *                  forall i = 0, ..., dim | 
     *                      el_i = <categories.get(i), dataSet.get(categories.get(i)), friends.get(categories.get(i))>
     *                      categories.get(i) != null
     * 
     * IR:          password != null && dim = categories.size() && 
     *              ( forall i = 1, ..., dim |
     *                      categories.get(i) != null 
     *                      && ( forall j = 0, ..., dim | categories.get(i) != categories.get(j) ) )
     *                      && ( forall k, l = 0, ..., dim | k != l => friends.get(categories.get(i)).get(k) != friends.get(categories.get(i)).get(l) )
     *                      && ( forall m, n = 0, ..., dim | m != n => dataSet.get(categories.get(i)).get(m) != dataSet.get(categories.get(i)).get(n) ) )
     *              && dataset.keySet() = friends.keySet()
     *                      
     */

    private int dim;
    private String password;
    private ArrayList<String> amiciAll=new ArrayList<String>();

    private HashMap<String, List<E>> dataSet;      //string rappresenterà il nome della category, List i post
    private HashMap<String, List<String>> friends;      

    //costruttore
    public Board2(String password)
    {
        this.password = password;
        this.dim = 0;
        this.dataSet = new HashMap<String, List<E>>();
        this.friends = new HashMap<String, List<String>>();
    }

    //Crea l’identità una categoria di dati
    
    public void createCategory(String Category, String passw) throws NullPointerException, InvalidPasswordException, ExistingCategoryException {
        if(Category == null) throw new NullPointerException();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(this.dataSet.containsKey(Category)) throw new ExistingCategoryException();

        dataSet.put(Category, new ArrayList<E>());
        friends.put(Category, new ArrayList<String>());
        this.dim++;
    }
    
    // Rimuove l’identità una categoria di dati
    
    public void removeCategory(String Category, String passw) throws InvalidCategoryExcetpion, InvalidPasswordException {
        if(Category == null) throw new NullPointerException();
        if(!this.dataSet.containsKey(Category)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        this.dataSet.remove(Category);
        this.friends.remove(Category);
    }

    // Aggiunge un amico ad una categoria di dati
    
    public void addFriend(String Category, String passw, String friend) throws InvalidCategoryExcetpion, ExistingFriendException, InvalidPasswordException {
        if(Category == null) throw new NullPointerException();
        if(!this.friends.containsKey(Category)) throw new InvalidCategoryExcetpion();
        if(this.friends.get(Category).contains(friend)) throw new ExistingFriendException();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        this.friends.get(Category).add(friend);
        addFriendGenerale(friend);
    }

    //rimuove un amico da una categoria 
    public void removeFriend(String Category, String passw, String friend) throws InvalidCategoryExcetpion, InvalidFriendException, InvalidPasswordException {
        if(!this.friends.containsKey(Category)) throw new InvalidCategoryExcetpion();
		if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(friend == null || Category == null) throw new NullPointerException();
        if(!this.friends.get(Category).contains(friend)) throw new InvalidFriendException();

        this.friends.get(Category).remove(friend);
    }
    
    // Inserisce un dato in bacheca
    // se vengono rispettati i controlli di identità

    public boolean put(String passw, E dato, String categoria) throws DuplicateDataException, InvalidCategoryExcetpion, InvalidPasswordException {
        if(!this.dataSet.containsKey(categoria)) throw new InvalidCategoryExcetpion();
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(this.dataSet.get(categoria).contains(dato)) throw new DuplicateDataException();
        if(dato == null || categoria == null) throw new NullPointerException();

        this.dataSet.get(categoria).add(dato);
        return this.dataSet.get(categoria).contains(dato);
    }
    
    // Ottiene una copia del del dato in bacheca
    // se vengono rispettati i controlli di identità

    public E get(String passw, E dato) throws InvalidDataException, InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        Collection<String> categories = this.dataSet.keySet();

        for(String category : categories) {
            if(this.dataSet.get(category).contains(dato))
                return (E) dato.cloneData();
        }

        throw new InvalidDataException();
    }

    // Rimuove il dato dalla bacheca
    // se vengono rispettati i controlli di identità

    public E remove(String passw, E dato) throws InvalidDataException, InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();

        boolean found = false;

        for(String category : this.dataSet.keySet()) {
            this.dataSet.get(category).remove(dato);
            found = true;
        }

        if(found) return dato;

        throw new InvalidDataException();
    }

    // Crea la lista dei dati in bacheca su una determinata categoria
    // se vengono rispettati i controlli di identità

    public List<E> getDataCategory(String passw, String Category) throws InvalidCategoryExcetpion, InvalidPasswordException {
        if(!this.password.equals(passw)) throw new InvalidPasswordException();
        if(Category == null) throw new NullPointerException();
        if(!this.dataSet.containsKey(Category)) throw new InvalidCategoryExcetpion();
        
        return this.dataSet.get(Category);
    }

    
    // restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca ordinati rispetto al numero di like.

    public Iterator<E> getIterator(String passw) {

        SortedSet<E> bacheca = new TreeSet<E>(new SortByLikes());

        for(String category : this.dataSet.keySet())
            bacheca.addAll(this.dataSet.get(category));
                
        return Collections.unmodifiableSortedSet(bacheca).iterator();
    }

    // Aggiunge un like a un dato

    public void insertLike(String friend, E dato) throws InvalidFriendException, InvalidDataException {

        if(friend == null || dato == null) throw new NullPointerException();

        boolean foundFriend = false, foundPost = false;

        for(String category : this.dataSet.keySet()) {
            if(this.friends.get(category).contains(friend)) {
                foundFriend = true;
                if(this.dataSet.get(category).contains(dato)) {
                    foundPost = true;
                    try {
                        dato.insertLike(friend);
                    } catch(DuplicateLikeException e) {
                        System.out.println(friend + " ha gia' messo like al post");
                    }
                    return;
                }
            }
        }

        if(!foundFriend) throw new InvalidFriendException();
        if(!foundPost) throw new InvalidDataException();
    }
    //aggiunge un amico alla lista generale degli amici
    public void addFriendGenerale(String friend)
    {
        if(!amiciAll.contains(friend))
            amiciAll.add(friend);
    }
    //rimuove un amico dalla lista generale degli amici
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

        for(String category : this.dataSet.keySet())
            if(friends.get(category).contains(friend)) bacheca.addAll(this.dataSet.get(category));

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
     * @param category t.c. this.dataSet.containsKey(Category)
     * @throws InvalidCategoryExcetpion if !this.dataSet.containsKey(Category)
     * @return this.friends.get(category).size()
     */
    public int numFriends(String category) throws InvalidCategoryExcetpion {
        return this.friends.get(category).size();
    }

    /**
     * 
     * @param category t.c. this.dataSet.containsKey(Category)
     * @throws InvalidCategoryExcetpion if !this.dataSet.containsKey(Category)
     * @return this.dataSet.get(category).size()
     */
    public int numData(String category) throws InvalidCategoryExcetpion {
        return this.dataSet.get(category).size();
    }

}