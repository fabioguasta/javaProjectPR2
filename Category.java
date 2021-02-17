import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Category<E extends Data<?>> {

    private String categoryName;
    private List<E> dataSet;
    private List<String> friends;

    public Category(String categoryName)
    {
        this.categoryName = categoryName;
        this.dataSet = new ArrayList<E>();
        this.friends = new ArrayList<String>();
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public List<E> getData()
    {
        return Collections.unmodifiableList(dataSet);
    }

    public boolean contains(E data)
    {
        return this.dataSet.contains(data);
    }

    public boolean contains(String friend)
    {
        return this.friends.contains(friend);
    }


    public List<String> getFriends()
    {
        return friends;
    }

    public void addFriend(String friend) throws ExistingFriendException {
        if(this.friends.contains(friend)) throw new ExistingFriendException();
        this.friends.add(friend);
    }

    public void addData(E data) throws DuplicateDataException {
        if(dataSet.contains(data)) throw new DuplicateDataException();

        this.dataSet.add(data);
    }

    public int numData() {
        return this.dataSet.size();
    }

    public int numFriends() {
        return this.friends.size();
    }

    public boolean removeDataIfExists(E data) {
        return this.dataSet.remove(data);
    }

    public boolean removeFriend(String friend) {
        return this.friends.remove(friend);
    }
}