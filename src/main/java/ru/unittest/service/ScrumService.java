package ru.unittest.service;


import ru.unittest.model.BacklogItem;

public interface ScrumService {
   void finishBacklogItem(int itemId, BacklogItem.Resolution resolution);
   void deleteBacklogItem(int itemId);
}
