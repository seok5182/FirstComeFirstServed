package com.sparta.firstcomefirstserved.controller;

import com.sparta.firstcomefirstserved.dto.ItemResponseDto;
import com.sparta.firstcomefirstserved.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@GetMapping("/items")
	public Page<ItemResponseDto> getItems(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc) {

		return itemService.getItems(page - 1, size, sortBy, isAsc);
	}

	@GetMapping("/items/{itemId}")
	public ItemResponseDto getItem(@PathVariable("itemId") Long itemId) {
		return itemService.getItem(itemId);
	}
}
