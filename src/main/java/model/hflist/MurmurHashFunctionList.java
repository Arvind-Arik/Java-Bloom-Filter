package model.hflist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.NonNull;


/**
 * MurmurHashFunctionList uses the murmur32 hash function as its hash function
 */
public class MurmurHashFunctionList implements HashFunctionList {

    @NonNull
    private List<HashFunction> hashFunctionList;

    /**
     * Default constructor for HashFunctionList with size set to 2
     */
    public MurmurHashFunctionList() {
        this.hashFunctionList = new ArrayList<>();
        setHashFunctionList(2);
    }

    /**
     * Constructor for HashFunctionList with size parameter
     * @param size Number of hash functions to be used
     */
    public MurmurHashFunctionList(int size) {
        this.hashFunctionList = new ArrayList<>();
        setHashFunctionList(size);
    }


    /**
     * Function to instantiate all HashFunction objects in list
     * @param size Number of hash functions to add to the list
     */
    private void setHashFunctionList(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Number of hash functions should be greater than 0");
        }

        Random rn = new Random();
        for (int i = 0; i < size; i++) {
            hashFunctionList.add(Hashing.murmur3_32(rn.nextInt()));
        }
    }


    /**
     * Get number of hash functions in the list
     *
     * @return Size of hash function list
     */
    public int getNumHashes() {
        return hashFunctionList.size();
    }


    /**
     * Override current HashFunctionList instance with a fresh one
     *
     * @param length New HashFunctionList instance length
     */
    public void setNewHashFunctionList(int length) {
        this.hashFunctionList = new ArrayList<>();
        setHashFunctionList(length);
    }


    /**
     * Returns an int list of hashes generated from given string
     *
     * @param key Term to get hashes from
     * @return List of int hashes
     */
    public ArrayList<Integer> getIntHashListFromString(String key) {

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        ArrayList<Integer> intHashList = new ArrayList<>();

        for (HashFunction hf : hashFunctionList) {
            intHashList.add(hf.newHasher()
                              .putString(key, Charsets.UTF_8)
                              .hash()
                              .asInt());
        }

        return intHashList;
    }


    /**
     * Returns an int list of bounded hashes generated from given string
     *
     * @param key Term to get hashes from
     * @return List of bounded int hashes
     */
    public ArrayList<Integer> getBoundedIntHashListFromString(String key, int bound) {

        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        if (bound < 5) {
            throw new IllegalArgumentException("Bound must be 5 or greater");
        }

        ArrayList<Integer> intHashList = new ArrayList<>();

        for (HashFunction hf : hashFunctionList) {
            int hash = hf.newHasher()
                         .putString(key, Charsets.UTF_8)
                         .hash()
                         .asInt();
            hash = (hash < 0) ? -1 * hash % bound : hash % bound;
            intHashList.add(hash);
        }

        return intHashList;
    }
}
