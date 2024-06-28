package com.kirkwoodwest.openwoods.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;

public class SerializationUtil {

	public static String serialize(Serializable o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(o);
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static Object deserialize(String s) {
		try {
			byte[] data = Base64.getDecoder().decode(s);
			ObjectInputStream ois = null;
			ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Generic method to deserialize a serialized string back to an ArrayList of type T
	public static <T> ArrayList<T> deserializeAsList(String serialized, Class<T> clazz) {
		try {
			Object deserializedObject = deserialize(serialized);
			if (deserializedObject instanceof ArrayList<?>) {
				ArrayList<?> arrayList = (ArrayList<?>) deserializedObject;
				ArrayList<T> result = new ArrayList<>();
				for (Object item : arrayList) {
					if (clazz.isInstance(item)) {
						result.add(clazz.cast(item));
					} else {
						throw new ClassCastException("Deserialized list contained an object that is not of type " + clazz.getSimpleName());
					}
				}
				return result;
			} else {
				throw new ClassCastException("Deserialized object is not an ArrayList");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
