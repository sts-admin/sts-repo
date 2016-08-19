package com.sts.core.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-08-18T12:46:37.123+0530")
@StaticMetamodel(State.class)
public class State_ {
	public static volatile SingularAttribute<State, Long> id;
	public static volatile SingularAttribute<State, Country> country;
	public static volatile SingularAttribute<State, String> stateCode;
	public static volatile SingularAttribute<State, String> stateName;
	public static volatile SingularAttribute<State, Boolean> archived;
}
