package dev.makeev.training_diary_app.dao.impl;

class DAOConstants {
    final static String ADD_LOG_EVENT_SQL =
            "INSERT INTO non_public.log_events (user_login, message) VALUES (?,?)";
    final static String GET_ALL_LOG_EVENTS_SQL = "SELECT * FROM non_public.log_events";
    final static String GET_LOG_EVENT_BY_LOGIN_SQL = GET_ALL_LOG_EVENTS_SQL + " WHERE user_login=?";

    final static String ADD_TRAINING_OF_USER_SQL =
            "INSERT INTO non_public.trainings " +
                    "(user_login, type_of_training_id, date, duration, calories_burned) " +
                    "VALUES (?,?,?,?,?)";
    final static String GET_ALL_TRAINING_OF_USER_SQL =
            "SELECT * FROM non_public.trainings";
    final static String GET_ALL_TRAINING_OF_USER_FOR_USER_SQL =
            GET_ALL_TRAINING_OF_USER_SQL + " WHERE user_login=? ORDER BY date";
    static final String UPDATE_TRAINING_OF_USER_SQL = "UPDATE non_public.trainings " +
            "SET type_of_training_id=?, date=?, duration=?, calories_burned=?" +
            " WHERE id=?";
    static final String DELETE_TRAINING_OF_USER_SQL =
            "DELETE FROM non_public.trainings WHERE id=?";
    static final String GET_ALL_TRAININGS_FOR_USER_BY_TYPE_OF_TRAINING_SQL =
            GET_ALL_TRAINING_OF_USER_SQL + " WHERE user_login=? AND type_of_training_id=? ORDER BY date";
    static final String ADD_ADDITIONAL_INFORMATION_FOR_TRAINING_OF_USER_SQL =
            "INSERT INTO non_public.additional_information " +
                    "(training_id, information, value) " +
                    "VALUES (?,?,?)";
    static final String GET_ADDITIONAL_INFORMATION_FOR_TRAINING_OF_USER_SQL =
            "SELECT * FROM non_public.additional_information WHERE training_id=?";
    static final String DELETE_ADDITIONAL_INFORMATION_TRAINING_OF_USER_SQL =
            "DELETE FROM non_public.additional_information WHERE training_id=?";

    final static String ADD_TYPE_OF_TRAINING_SQL =
            "INSERT INTO non_public.types_of_training (type) VALUES (?)";
    final static String GET_ALL_TYPES_OF_TRAINING_SQL = "SELECT * FROM non_public.types_of_training";
    final static String GET_TYPE_OF_TRAINING_BY_ID_SQL = GET_ALL_TYPES_OF_TRAINING_SQL + " WHERE id=?";
    static final String GET_TYPE_OF_TRAINING_BY_TYPE_SQL = GET_ALL_TYPES_OF_TRAINING_SQL + " WHERE type=?";

    final static String ADD_USER_SQL =
            "INSERT INTO non_public.users (login, password, admin) VALUES (?,?,?)";
    final static String GET_ALL_USERS_SQL = "SELECT * FROM non_public.users";
    final static String GET_USER_BY_LOGIN_SQL = GET_ALL_USERS_SQL + " WHERE login=?";
}
