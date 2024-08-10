package test;

import api.TrelloApi;

import org.junit.jupiter.api.*;
import utils.OzelListe;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrelloTest {

    private static String boardId;
    private static String listId;
    private static String firstCardId;
    private static String secondCardId;
    private static boolean isBoardCreated = true;
    private static boolean isListCreated = true;

    @Test
    @Order(0)
    public void createBoard() {
        String boardNameToCreate = "Enes Board";
        OzelListe customDataList = TrelloApi.createBoard(boardNameToCreate);
        boardId = customDataList.getId();
        String createdBoardName = customDataList.getName();

        if (boardId == null) {
            System.out.println("BoardId bos.");
            isBoardCreated = false;
            Assertions.fail("Board olusturma basarisiz.");
        }
        Assertions.assertEquals(createdBoardName, boardNameToCreate);
        System.out.println("Board olusturulmasi basarili oldu, board'un adi : " + createdBoardName);
    }

    @Test
    @Order(1)
    public void addListToBoard() {
        assumeTrue(isBoardCreated);
        String listNameToCreate = "Liste";
        OzelListe customDataList = TrelloApi.createList(boardId, listNameToCreate);
        listId = customDataList.getId();
        if (listId == null) {
            System.out.println("Liste bos.");
            isListCreated = false;
            Assertions.fail("List olusturma basarisiz.");
        }
        String createdListName = customDataList.getName();
        Assertions.assertEquals(createdListName, listNameToCreate);
        System.out.println("Liste olusturma basarili, liste adi: " + createdListName);
    }

    @Test
    @Order(2)
    public void addCardsToList() {
        assumeTrue(isListCreated);

        String firstCardNameToCreate = "Birinci Kart";
        String secondCardNameToCreate = "Ikinci Kart";
        OzelListe customDataListForFirstCard = TrelloApi.createCard(listId, firstCardNameToCreate);
        OzelListe customDataListForSecondCard = TrelloApi.createCard(listId, secondCardNameToCreate);

        String createdFirstCardName = customDataListForFirstCard.getName();
        firstCardId = customDataListForFirstCard.getId();
        String createdSecondCardName = customDataListForSecondCard.getName();
        secondCardId = customDataListForSecondCard.getId();

        Assertions.assertEquals(firstCardNameToCreate, createdFirstCardName);
        Assertions.assertEquals(secondCardNameToCreate, createdSecondCardName);
        System.out.println("Iki kart listeye basariyla eklendi.");
    }

    @Test
    @Order(3)
    public void updateRandomCard() {
        String updatedCardName = "UpdatedCard";
        String updatedCardId = TrelloApi.updateRandomCard(boardId, updatedCardName);
        Assertions.assertNotNull(updatedCardId);
        System.out.printf("Card adi eklendi", updatedCardName);
    }

    @Test
    @Order(4)
    public void deleteCards() {
        TrelloApi.deleteCard(firstCardId);
        TrelloApi.deleteCard(secondCardId);
        System.out.println("Cardlar basariyla silindi");
    }

    @Test
    @Order(5)
    public void deleteBoard() {
        TrelloApi.deleteBoard(boardId);
        System.out.println("Board basariyla silindi");
    }
}