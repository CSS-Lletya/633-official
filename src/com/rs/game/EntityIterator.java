package com.rs.game;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public final class EntityIterator<T extends Entity> implements Iterator<T> {
	
	private EntityList<T> entityList;
	
	/**
	 * The previous index of this iterator.
	 */
	private int previousIndex = -1;
	
	/**
	 * The current index of this iterator.
	 */
	private int index = 0;
	
	EntityIterator(EntityList<T> entityList) {
		this.entityList = entityList;
	}
	
	@Override
	public boolean hasNext() {
		for (int i = index; i < entityList.getEntities().length; i++) {
			if (entityList.getEntities()[i] != null) {
				index = i;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public T next() {
		T entity = null;
		for (int i = index; i < entityList.getEntities().length; i++) {
			if (entityList.getEntities()[i] != null) {
				entity = (T) entityList.getEntities()[i];
				index = i;
				break;
			}
		}
		if (entity == null) {
			throw new NoSuchElementException();
		}
		previousIndex = index;
		index++;
		return entity;
	}
	
	@Override
	public void remove() {
		if (previousIndex == -1) {
			throw new IllegalStateException();
		}
		entityList.remove(entityList.getEntities()[previousIndex]);
		previousIndex = -1;
	}
	
}
