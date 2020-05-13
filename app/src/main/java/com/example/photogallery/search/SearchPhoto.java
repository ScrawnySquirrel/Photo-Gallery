package com.example.photogallery.search;

import com.example.photogallery.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPhoto {

    public static String search(ArrayList<String> imagePaths, String searchInput, String startDate, String endDate) {
        ArrayList<String> results = new ArrayList<>();
        String currentPhotoPath = null;

        if (!searchInput.isEmpty()){
            results = searchPhotoByCaption(imagePaths, searchInput);
        }else if (startDate != null || endDate != null){
            results = searchPhotoByDate(imagePaths, startDate, endDate);
        }

        if (!results.isEmpty()){
            currentPhotoPath = results.get(0);
        }

        return currentPhotoPath;
    }

    private static ArrayList<String> searchPhotoByCaption(List<String> imagePaths, String searchInput){
        ArrayList<String> results = new ArrayList<>();

        for (String file : imagePaths) {
            if(file.contains(searchInput)){
                results.add(file);
            }
        }

        return results;
    }

    private static ArrayList<String> searchPhotoByDate(List<String> imagePaths, String startDate, String endDate){
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> imageDates = new ArrayList<>();

        Collections.sort(imagePaths);

        for (String path : imagePaths) {
            imageDates.add(MainActivity.getPhotoDate(path));
        }

        int firstIndex = imageDates.indexOf(startDate);
        int lastIndex = imageDates.lastIndexOf(endDate);

        if (firstIndex == -1 && lastIndex == -1){
            return results;
        }

        if (firstIndex != -1 && lastIndex == -1){
            results.add(imagePaths.get(firstIndex));
        }

        if(firstIndex != -1 && lastIndex != -1){
            if(lastIndex >= firstIndex){
                for(int i = firstIndex; i <= lastIndex; i++){
                    results.add(imagePaths.get(i));
                }
            }else{
                // TODO: return error
            }
        }

        if(firstIndex == -1 && lastIndex != -1){
            results.add(imagePaths.get(lastIndex));
        }

        // TODO: need to add more scenarios

        return results;
    }
}
