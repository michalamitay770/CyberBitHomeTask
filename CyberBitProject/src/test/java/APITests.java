import static io.restassured.RestAssured.*;

import common.helpers.DateHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

public class APITests {

  RequestSpecification httpRequest;

  @BeforeMethod
  void beforeMethod() {
    baseURI = "https://reqres.in";
    basePath = "/api/users";
    httpRequest = given();
  }

  @Test(description = "Entire users list test")
  void test1() {
    Response response = httpRequest.get("?page=1");
    Assert.assertEquals(response.path("total"), 12, "validate total users number");
    Assert.assertEquals(response.getStatusCode(), 200, "validate status code");

    User[] usersArray1 = response.jsonPath().getObject("data", User[].class);
    response = httpRequest.get("?page=2");
    User[] usersArray2 = response.jsonPath().getObject("data", User[].class);
    Assert.assertTrue(
        isTraceyRamosExist(usersArray1) || isTraceyRamosExist(usersArray2),
        "validate Tracey Ramos is in the list");
  }

  @Test(description = "Single user test")
  void test2() {
    Response response = httpRequest.get("/2");
    Assert.assertEquals(response.getStatusCode(), 200, "validate status code");

    JsonPath jsonPath = JsonPath.from(response.getBody().asString());
    User user = jsonPath.getObject("data", User.class);
    Assert.assertEquals(user.getId(), 2, "validate user's id");
    Assert.assertEquals(user.getEmail(), "janet.weaver@reqres.in", "validate user's email");
    Assert.assertEquals(user.getFirstName(), "Janet", "validate user's first_name");
    Assert.assertEquals(user.getLastName(), "Weaver", "validate user's last_name");
    Assert.assertEquals(
        user.getAvatar(), "https://reqres.in/img/faces/2-image.jpg", "validate user's avatar");
  }

  @Test(description = "Create a new user test")
  void test3() {
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "David");
    requestParams.put("job", "Teacher");
    httpRequest.body(requestParams);
    Response response = httpRequest.post();

    Assert.assertTrue(
        DateHelper.compareDatesWithRound(
            DateHelper.getFormattedDate(response.path("createdAt").toString()), DateHelper.now()),
        "validate createdAt");
    Assert.assertEquals(response.getStatusCode(), 201, "validate status code");
    // NOTE: POST response not contains name&job (UI is not updated)
    // I tried to validate name&job by GET user by id=2 API, but data not saved from call to another
    // call
  }

  @Test(description = "Update user test")
  void test4() {
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "David");
    requestParams.put("job", "Teacher");
    httpRequest.body(requestParams);
    Response response = httpRequest.put("/2");

    Assert.assertTrue(
        DateHelper.compareDatesWithRound(
            DateHelper.getFormattedDate(response.path("updatedAt").toString()), DateHelper.now()),
        "validate createdAt");
    Assert.assertEquals(response.getStatusCode(), 200, "validate status code");
    System.out.println(response.getBody().asString());
    // NOTE: PUT response not contains name&job (UI is not updated)
    // I tried to validate name&job by GET user by id=2 API, but data not saved from call to another
    // call
  }

  /** ********************************* PRIVATE METHODS ************************************** */
  private boolean isTraceyRamosExist(User[] usersArray) {
    boolean isTraceyRamosExist = false;
    for (int i = 0; i < usersArray.length; i++) {
      if (usersArray[i].getFirstName().equals("Tracey")
          && usersArray[i].getLastName().equals("Ramos")) isTraceyRamosExist = true;
    }
    return isTraceyRamosExist;
  }
}
