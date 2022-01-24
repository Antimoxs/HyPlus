package dev.antimoxs.hypixelapi.events;

import dev.antimoxs.hypixelapi.response.*;


public interface IHypixelApiEvent {

    default void onApiResponse(ApiResponse apiResponse) {}

    default void onApiRequestError(String errorMessage) {}
    default void onApiParseError(String errorMessage, ApiResponse apiResponse) {}

    default void onApiRequestNotSuccessful(BaseResponse response) {}
    default void onApiFriendsResponse(FriendsResponse response) {}
    default void onApiGamesResponse(GamesResponse response) {}
    default void onApiGuildResponse(GuildResponse response) {}
    default void onApiKeyResponse(KeyResponse response) {}
    default void onApiPlayerResponse(PlayerResponse response) {}
    default void onApiQuestsResponse(QuestsResponse response) {}
    default void onApiStatusResponse(StatusResponse response) {}

    // Async events

    default void onAsyncApiResponse(ApiResponse apiResponse) {}

    default void onAsyncApiRequestError(String errorMessage) {}
    default void onAsyncApiParseError(String errorMessage, ApiResponse apiResponse) {}

    default void onAsyncApiRequestNotSuccessful(BaseResponse response) {}
    default void onAsyncApiFriendsResponse(FriendsResponse response) {}
    default void onAsyncApiGamesResponse(GamesResponse response) {}
    default void onAsyncApiGuildResponse(GuildResponse response) {}
    default void onAsyncApiKeyResponse(KeyResponse response) {}
    default void onAsyncApiPlayerResponse(PlayerResponse response) {}
    default void onAsyncApiQuestsResponse(QuestsResponse response) {}
    default void onAsyncApiStatusResponse(StatusResponse response) {}

}
