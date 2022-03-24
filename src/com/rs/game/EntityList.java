package com.rs.game;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

import lombok.Getter;

public class EntityList<T extends Entity> implements Iterable<T> {
	
	/**
	 * The array of entities
	 */
	@Getter
	private T[] entities;
	
	/**
	 * The lowest free index available
	 */
	@Getter
	private int lowestFreeIndex;
	
	/**
	 * The size of entities
	 */
	@Getter
	private int size;
	
	@SuppressWarnings("unchecked")
	public EntityList(int capacity, boolean player) {
		entities = (T[]) (player ? new Player[capacity] : new NPC[capacity]);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new EntityIterator<T>(this);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(entities);
	}
	
	/**
	 * Adds an entity to the list
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean add(T entity) {
		synchronized (this) {
			entity.setIndex(lowestFreeIndex + 1);
			entities[lowestFreeIndex] = entity;
			size++;
			for (int i = lowestFreeIndex + 1; i < entities.length; i++) {
				if (entities[i] == null) {
					lowestFreeIndex = i;
					break;
				}
			}
			return true;
		}
	}
	
	/**
	 * Removes an entity to the list
	 *
	 * @param entity
	 * 		The entity
	 */
	public void remove(T entity) {
		synchronized (this) {
			int listIndex = entity.getIndex() - 1;
			entities[listIndex] = null;
			size--;
			if (listIndex < lowestFreeIndex) {
				lowestFreeIndex = listIndex;
			}
		}
	}
	
	/**
	 * Gets an entity from the list
	 *
	 * @param index
	 * 		The entity
	 */
	public T get(int index) {
		if (index >= entities.length || index == 0) {
			return null;
		}
		return entities[index - 1];
	}
	
	/**
	 * If the list contains an entity
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean contains(T entity) {
		return entity.getIndex() != 0 && entities[entity.getIndex() - 1] == entity;
	}
	
	/**
	 * Gets the size of the entities
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Converts the array to a {@code Stream} {@code Object}
	 */
	public Stream<T> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
}