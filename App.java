import java.util.Iterator;
import java.util.List;

public class App {
    private static DataBoard<myData<?>> board;

    public static void main(String[] args) {

        /************************ /
        /      Prima batteria     / 
        /         di test         / 
        /*************************/
        System.out.println();
        System.out.println("Prima batteria di test:");
        board = new Board<myData<?>>("Password-1");
        test();


        /************************ /
        /     Seconda batteria    / 
        /         di test         / 
        /*************************/
        System.out.println();
        System.out.println("Seconda batteria di test:");
        board = new Board2<myData<?>>("Password-1");
        test();
    }

    private static void test() {
        // Posts
        myData<String> post1 = new myData<String>("Ciao a tutti, è una bella giornata");
        myData<String> post2 = new myData<String>("Buongiorno");
        myData<String> post3 = new myData<String>("Oggi è bel tempo");

        // Freinds
        String friend1 = "Mario";
        String friend2 = "Franco";

        createCategory("password sbagliata", "saluti");     // Lancia un messaggio di password errata
        createCategory("Password-1", "saluti");               // Crea la categoria saluti
        createCategory("Password-1", "tempo");                // Crea la categoria tempo
        createCategory("Password-1", "tempo");                // Lancia un'eccezione di categoria già esistente
        addFriend("Password-1", friend1, "saluti");           // Aggiunge l'amico Mario a saluti
        addFriend("Password-1", friend1, "tempo");            // Aggiunge l'amico Mario a tempo
        addFriend("Password-1", friend2, "saluti");           // Aggiunge l'amico Franco a saluti
        addPost("Password-1", post1, "saluti");               // Aggiunge post "Ciao a tutti, è una bella giornata" a saluti
        addPost("Password-1", post2, "saluti");               // Aggiunge post "Buongiorno a tutti" a saluti 
        addPost("Password-1", post1, "tempo");                // Aggiunge post "Ciao a tutti, è una bella giornata" a tempo
        addPost("Password-1", post3, "tempo");                // Aggiunge post "Oggi è bel tempo" a tempo
        addPost("Password-1", post3, "tempo");                // Lancia un'eccezione di post già presente nella categoria
        addPost("Password-1", getPost("Password-1", post3), "tempo");                // L'eccezione non viene lanciata perché la get restituisce una deep copy del dato
        insertLike(friend1, post1);                         // Mario aggiunge like a "Ciao a tutti, è una bella giornata"
        insertLike(friend1, post2);                         // Mario aggiunge like a "Buongiorno"
        insertLike(friend2, post2);                         // Franco aggiunge like a "Buongiorno"

        Iterator<myData<?>> iterator =  getIterator("Password-1");        // Prende iteratore della bacheca e scorre
        System.out.println("Tutti i post in bacheca");
        if(iterator != null)
            while(iterator.hasNext())
                iterator.next().Display();

        System.out.println();
        System.out.println("Post visibili all'amico " + friend1);       // Prende iteratore dei post visibili all'amico Mario e scorre
        iterator = getFriendIterator(friend1);
        if(iterator != null)
            while(iterator.hasNext())
                iterator.next().Display();

        System.out.println();
        System.out.println("Post visibili all'amico " + friend2);       // Prende iteratore dei post visibili all'amico Franco e scorre
        iterator = getFriendIterator(friend2);
        if(iterator != null)
            while(iterator.hasNext())
                iterator.next().Display();
                
        removeCategory("Password-1", "saluti");                           // Rimuove la categoria saluti
        getDataCategory("Password-1", "saluti");                          // Lancia un'eccezione perché saluti non esiste più
        removePost("Password-1", post1, "tempo");                         // Rimuove il post "Ciao a tutti, è una bella giornata" da tempo
        removeFriend("Password-1", friend1, "tempo");                     // Rimuove l'amico mario da tempo

        iterator = getFriendIterator(friend1);                          // Lancia un'eccezione perché Mario non è più presente in nessuna delle categorie
    }

    private static void addPost(String password, myData<?> post, String category) {
        try {
            board.put(password, post, category);
        } catch (DuplicateDataException e) {
            System.out.println("Dato gia' presente nella categoria " + category);
        } catch (InvalidCategoryExcetpion e) {
            System.out.println("Categoria " + category + " non esistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        }
    }

    private static void removePost(String password, myData<?> post, String category) {
        try {
            board.remove(password, post);
        } catch (InvalidDataException e) {
            System.out.println("Dato inesistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        }
    }
    
    private static myData<?> getPost(String password, myData<?> post) {
        myData<?> dato = null;
        
        try {
            dato = board.get(password, post);
        } catch (InvalidDataException e) {
            System.out.println("Dato inesistente");
            return null;
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
            return null;
        }

        return dato;
    }

    private static void createCategory(String password, String categoryName) {
        try {
            board.createCategory(categoryName, password);
        } catch (ExistingCategoryException e) {
            System.out.println("Categoria " + categoryName + " gia' esistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        }
    }

    private static void removeCategory(String password, String category) {
        try {
            board.removeCategory(category, password);
        } catch (InvalidCategoryExcetpion e) {
            System.out.println("Categoria " + category + " non esistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        }
    }

    private static void addFriend(String password, String friend, String category) {
        try {
            board.addFriend(category, password, friend);
        } catch (InvalidCategoryExcetpion e) {
            System.out.println("Categoria " + category + " non esistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        } catch (ExistingFriendException e) {
            System.out.println(friend + " gia' presente in" + category);
        }
    }

    private static void removeFriend(String password, String friend, String category) {
        try {
            board.removeFriend(category, password, friend);
        } catch (InvalidCategoryExcetpion e) {
            System.out.println("Categoria " + category + " non esistente");
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
        } catch (InvalidFriendException e) {
            System.out.println(friend + " non presente nella categoria " + category);
        }
    }

    private static List<myData<?>> getDataCategory(String password, String category) {
        List<myData<?>> dataCategory = null;
        
        try {
            dataCategory = board.getDataCategory(password, category);
        } catch (InvalidCategoryExcetpion e) {
            System.out.println("Categoria " + category + " non esistente");
            return null;
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
            return null;
        }

        return dataCategory;
    }

    private static Iterator<myData<?>> getIterator(String password) {
        Iterator<myData<?>> iterator = null;
        
        try {
            iterator = board.getIterator(password);
        } catch (InvalidPasswordException e) {
            System.out.println("Password errata");
            return null;
        }

        return iterator;
    }

    private static void insertLike(String friend, myData<?> dato) {        
        try {
            board.insertLike(friend, dato);
        } catch (InvalidDataException e) {
            System.out.println("Dato inesistente");
        } catch (InvalidFriendException e) {
            System.out.println(friend + " inesistente");
        } catch (DuplicateLikeException e) {
            System.out.println("L'amico " + friend + " ha già messo like al post");
        }
    }

    private static Iterator<myData<?>> getFriendIterator(String friend) {
        Iterator<myData<?>> iterator = null;
        
        try {
            iterator = board.getFriendIterator(friend);
        } catch (InvalidFriendException e) {
            System.out.println(friend + " inesistente");
            return null;
        }

        return iterator;
    }
}
