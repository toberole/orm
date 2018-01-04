// IBookManager.aidl
package com.zhouwei.helloapt.aidl;

// Declare any non-default types here with import statements
import com.zhouwei.helloapt.aidl.Book;

interface IBookManager {
    List<Book> getBooks();
    void add(in Book book);
}
