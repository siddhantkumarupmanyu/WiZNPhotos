package sku.challenge.wiznphotos.test_utils

import sku.challenge.wiznphotos.vo.PhotoItem

object DummyData {

    fun photoItems(startId: Int, noOfItems: Int): List<PhotoItem> {
        val items = (startId until (startId + noOfItems)).map { id: Int ->
            PhotoItem(
                id,
                "title: $id",
                "https://example.com/dummy/$id.jpg"
            )
        }

        return items
    }

}