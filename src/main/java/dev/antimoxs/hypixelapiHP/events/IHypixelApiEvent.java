package dev.antimoxs.hypixelapiHP.events;

import dev.antimoxs.hypixelapiHP.response.ApiResponse;


public interface IHypixelApiEvent {

    default void onApiResponse(dev.antimoxs.hypixelapiHP.response.ApiResponse apiResponse) {}

    default void onApiRequestError(String errorMessage) {}
    default void onApiParseError(String errorMessage, dev.antimoxs.hypixelapiHP.response.ApiResponse apiResponse) {}

    default void onApiRequestNotSuccessful(dev.antimoxs.hypixelapiHP.response.BaseResponse response) {}
    default void onApiFriendsResponse(dev.antimoxs.hypixelapiHP.response.FriendsResponse response) {}
    default void onApiGamesResponse(dev.antimoxs.hypixelapiHP.response.GamesResponse response) {}
    default void onApiGuildResponse(dev.antimoxs.hypixelapiHP.response.GuildResponse response) {}
    default void onApiKeyResponse(dev.antimoxs.hypixelapiHP.response.KeyResponse response) {}
    default void onApiPlayerResponse(dev.antimoxs.hypixelapiHP.response.PlayerResponse response) {}
    default void onApiQuestsResponse(dev.antimoxs.hypixelapiHP.response.QuestsResponse response) {}
    default void onApiStatusResponse(dev.antimoxs.hypixelapiHP.response.StatusResponse response) {}

    // Async events

    default void onAsyncApiResponse(dev.antimoxs.hypixelapiHP.response.ApiResponse apiResponse) {}

    default void onAsyncApiRequestError(String errorMessage) {}
    default void onAsyncApiParseError(String errorMessage, ApiResponse apiResponse) {}

    default void onAsyncApiRequestNotSuccessful(dev.antimoxs.hypixelapiHP.response.BaseResponse response) {}
    default void onAsyncApiFriendsResponse(dev.antimoxs.hypixelapiHP.response.FriendsResponse response) {}
    default void onAsyncApiGamesResponse(dev.antimoxs.hypixelapiHP.response.GamesResponse response) {}
    default void onAsyncApiGuildResponse(dev.antimoxs.hypixelapiHP.response.GuildResponse response) {}
    default void onAsyncApiKeyResponse(dev.antimoxs.hypixelapiHP.response.KeyResponse response) {}
    default void onAsyncApiPlayerResponse(dev.antimoxs.hypixelapiHP.response.PlayerResponse response) {}
    default void onAsyncApiQuestsResponse(dev.antimoxs.hypixelapiHP.response.QuestsResponse response) {}
    default void onAsyncApiStatusResponse(dev.antimoxs.hypixelapiHP.response.StatusResponse response) {}

}
