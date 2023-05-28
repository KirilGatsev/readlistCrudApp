package com.simpleCRUD.app.readListCRUD.Services;

import org.springframework.stereotype.Component;

@Component
public class BookValidation {
    public boolean validTotalPages(int pages){return pages > 0;}

    public boolean validReadPages(int readPages, int totalPages){return readPages >= 0 && readPages <= totalPages;}

    public boolean validName(String name){return name != null;}
}
