package com.sprint.mission.discodeit.factory;

/**
 * 팩토리 클래스에서 객체를 생성할 때 영속화하는 방식을 나누는 ENUM
 * <ul>
 *   <li>JCF: JCF</li>
 *   <li>FILE: File I/O</li>
 * </ul>
 */
public enum PersistenceType {
    JCF, FILE;
}
