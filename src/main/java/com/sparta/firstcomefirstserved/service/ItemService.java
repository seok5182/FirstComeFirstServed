package com.sparta.firstcomefirstserved.service;

import com.sparta.firstcomefirstserved.dto.ItemResponseDto;
import com.sparta.firstcomefirstserved.entity.Item;
import com.sparta.firstcomefirstserved.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Transactional(readOnly = true)
	public Page<ItemResponseDto> getItems(int page, int size, String sortBy, boolean isAsc) {
		Sort.Direction direction = isAsc ? Sort.Direction.ASC : Direction.DESC;
		//Sort.Direction direction = Direction.ASC;
		Sort sort = Sort.by(direction, sortBy);
		//Sort sort = Sort.by(direction, "name");
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Item> itemList;

		itemList = itemRepository.findAll(pageable);

		return itemList.map(ItemResponseDto::new);
	}

	public ItemResponseDto getItem(Long itemId) {
		return new ItemResponseDto(itemRepository.findById(itemId));
	}
}
