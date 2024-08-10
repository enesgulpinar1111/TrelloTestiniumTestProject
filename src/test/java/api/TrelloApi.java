package api;

import io.restassured.response.Response;
import io.restassured.response.ExtractableResponse;
import io.restassured.http.ContentType;;
import utils.OzelListe;

import java.util.Random;
import static io.restassured.RestAssured.given;

public class TrelloApi {

    private static final String API_KEY = "b484fff388e74c27cc90a61b71b9f1c8";
    private static final String TOKEN = "ATTA45167907ee0386d80198dec0855f5e09079f68fbcdddb138434eceb8ae3ccb47FB19CA57";


    public static OzelListe createBoard(String boardName) {
        Response response = given()
                .queryParam("name", boardName)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/");

        ExtractableResponse<Response> extractableResponse = response.then()
                .statusCode(200)
                .extract();

        String boardId = extractableResponse.path("id");
        String createdBoardName = extractableResponse.path("name");

        return new OzelListe(boardId, createdBoardName);
    }

    public static OzelListe createList(String boardId, String listName) {
        Response response = given()
                .queryParam("name", listName)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/" + boardId + "/lists");

        String id = response.jsonPath().getString("id");
        String createdListName = response.jsonPath().getString("name");

        return new OzelListe(id, createdListName);
    }

    public static OzelListe createCard(String listId, String cardName) {
        Response response = given()
                .queryParam("name", cardName)
                .queryParam("idList", listId)
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/cards");

        ExtractableResponse<Response> extractableResponse = response.then()
                .statusCode(200)
                .extract();

        String cardId = extractableResponse.path("id");
        String createdCardName = extractableResponse.path("name");

        return new OzelListe(cardId, createdCardName);
    }

    public static String updateRandomCard(String boardId, String updateCardName) {

        Response response = given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .get("https://api.trello.com/1/boards/" + boardId + "/cards");


        Random rand = new Random();
        String cardId = (String) response.jsonPath().getList("id").get(rand.nextInt(response.jsonPath().getList("id").size()));


        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .queryParam("name", updateCardName)
                .contentType(ContentType.JSON)
                .when()
                .put("https://api.trello.com/1/cards/" + cardId)
                .then()
                .statusCode(200);

        return cardId;
    }

    public static void deleteCard(String cardId) {
        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/cards/" + cardId)
                .then()
                .statusCode(200);
    }

    public static void deleteBoard(String boardId) {
        given()
                .queryParam("key", API_KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(200);
    }
}