package ru.bot.HelperBot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;
import ru.bot.HelperBot.vacancy.domain.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class temp {
    public static void main(String[] args) throws JsonProcessingException {

        final String uri = "https://api.hh.ru/vacancies";

//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(uri))
//                .timeout(Duration.ofSeconds(2))
//                .GET()
//                .build();


        RestClient restClient = RestClient.create();
        String responseClient = restClient.get()
                .uri(uri, uriBuilder -> uriBuilder
                        .queryParam("per_page", 5)
                        .queryParam("page", 3)
                        .queryParam("text", "java")
                        .build())
                .retrieve()
                .body(String.class);

        System.out.println(responseClient);
        System.out.println("//////////////////");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(responseClient);

        List<Vacancy> vacancies = new ArrayList<>();

        boolean flagFindVacancy = false;

        for (int i = 0; i < rootNode.get("items").size(); i++) {
            flagFindVacancy = true;
            Vacancy vacancy = new Vacancy();
            var item = rootNode.get("items").get(i);
            vacancy.setIdVacancy(Integer.parseInt(item.get("id").asText()));
            vacancy.setNameVacancy(item.get("name").asText());
            vacancy.setSalary(String.valueOf(item.get("salary_range")));
            vacancy.setCity(String.valueOf(item.get("area").get("name")));
            vacancy.setCompany(String.valueOf(item.get("area")));
            vacancies.add(vacancy);
        }
        if(!flagFindVacancy) {
            System.out.println("Vacancy was not found");
        } else {
            vacancies.stream().forEach(x -> System.out.println(x.toString()));
        }
    }


}
