/**
 * @author     Derrick Lee <stjohndlee@gmail.com>
 */

public class MyHashMap<V> {
	private MyHashEntry[] hashtable;
	private int capacity;
	private int size = 0;
	
	/*
	 * static inner class for elements to be inserted into the hash map
	 */
	public static class MyHashEntry {
		private MyHashEntry next;
		private String key;
		private Object value;
		
		MyHashEntry(String key, Object value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}
	
	/*
	 * Constructor for MyHashMap
	 */
	public MyHashMap(int capacity) {
		if (capacity <= 0 ) throw new IllegalArgumentException();
		this.capacity = capacity;
		this.hashtable = new MyHashEntry[capacity];
	}
	
	private int findInd(String key) {
		return mod(key.hashCode(), capacity);
	}
	
	private int mod(int n, int m) {
		return ((n % m) + m) % m; // to deal with negative hash codes
	} 
	
	/**
	 * @param key    string key to be use in the map. In case of duplicate keys, the former value is overridden
	 * @param value  value associated with the key. Could be any Java object
	 * @return       true if insertion succeeds, false otherwise
	 */
	public boolean set(String key, V value) {
		// if the hash map is full, reject
		if (size == capacity) return false;
		
		int ind = findInd(key);
		
		if (hashtable[ind] == null) {	// no element in the bucket
			hashtable[ind] = new MyHashEntry(key, (Object) value);
			size++;
			return true;
		} else {    // if there is an element in the bucket
			MyHashEntry curr = hashtable[ind];
			while (curr.next != null) {
				if (!curr.key.equals(key)) {
					curr = curr.next;
				} else {
					// if there is an element with the same key
					// override it
					curr.value = (Object) value;
					return true;
				}
			}
			// reached the end of the linked list
			if (!curr.key.equals(key)) {
				curr.next = new MyHashEntry(key, (Object) value);
				size++;
			} else {
				curr.value = (Object) value;
			}
			return true;
		}
	}
	
	/**
	 * @param key   key for retrieving the value associated with it
	 * @return      value associated with the key
	 */
	@SuppressWarnings("unchecked")
	public V get(String key) {
		int ind = findInd(key);
		if (hashtable[ind] == null) return null;
		MyHashEntry curr = hashtable[ind];
		while (curr.next != null) {
			if (!curr.key.equals(key)) {
				// if the keys don't match, keep moving down the linked list
				curr = curr.next;
			} else {
				// if the match is found, return the value
				return (V) curr.value; // unchecked casting
			}
		}
		// reached the end of the linked list
		return curr.key.equals(key)? (V) curr.value : null;
	}
	
	/**
	 * @param key   key for deleting the pair
	 * @return      value of the key-value pair that's been deleted, null if such pair doesn't exist
	 */
	@SuppressWarnings("unchecked")
	public V delete(String key) {
		int ind = findInd(key);
		
		if (hashtable[ind] == null) return null;
		
		MyHashEntry curr = hashtable[ind];
		MyHashEntry prev = null;
		
		while (curr.next != null) {
			if (!curr.key.equals(key)) {
				// if the keys don't match, keep moving down the linked list
				curr = curr.next;
				prev = curr;
			} else {
				// if the match is found, delete the entry from the linked list and return the value
				prev.next = curr.next;
				curr.next = null;
				size--;
				return (V) curr.value; // unchecked casting
			}
		}
		
		// reached the end of the linked list
		if (!curr.key.equals(key)) {
			// if it's not a match
			return null;
		} else {
			if (prev == null) {
				// if this was a single-element linked list
				hashtable[ind] = null;
				size--;
				return (V) curr.value; // unchecked casting
			} else {
				prev.next = null;
				size--;
				return (V) curr.value; // unchecked casting
			}
		}
	}
	
	/**
	 * @return   the current load factor of the hash map
	 */
	public double load() {
		return (1.0 * size) / capacity;
	}

}