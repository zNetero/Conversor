package org.example;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.*;

public class ConversorMoedas {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] opcoes = {
                "1. USD para EUR",
                "2. EUR para USD",
                "3. USD para BRL",
                "4. BRL para USD",
                "5. EUR para BRL",
                "6. BRL para EUR",
                "7. Sair"
        };

        while (true) {
            System.out.println("\n=== Conversor de Moedas ===");
            for (String opcao : opcoes) {
                System.out.println(opcao);
            }

            System.out.print("Escolha uma opção: ");
            int escolha = scanner.nextInt();

            if (escolha == 7) {
                System.out.println("Encerrando o conversor...");
                break;
            }

            String moedaOrigem = "";
            String moedaDestino = "";

            switch (escolha) {
                case 1 -> { moedaOrigem = "USD"; moedaDestino = "EUR"; }
                case 2 -> { moedaOrigem = "EUR"; moedaDestino = "USD"; }
                case 3 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                case 4 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
                case 5 -> { moedaOrigem = "EUR"; moedaDestino = "BRL"; }
                case 6 -> { moedaOrigem = "BRL"; moedaDestino = "EUR"; }
                default -> {
                    System.out.println("Opção inválida!");
                    continue;
                }
            }

            System.out.print("Digite o valor a converter: ");
            double valor = scanner.nextDouble();

            double resultado = converterMoeda(moedaOrigem, moedaDestino, valor);
            if (resultado >= 0) {
                System.out.printf("Resultado: %.2f %s = %.2f %s%n",
                        valor, moedaOrigem, resultado, moedaDestino);
            } else {
                System.out.println("Erro ao converter moeda.");
            }
        }

        scanner.close();
    }

    public static double converterMoeda(String de, String para, double valor) {
        try {
            URL url = new URL(API_URL + de);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            JsonObject taxas = json.getAsJsonObject("rates");
            double taxa = taxas.get(para).getAsDouble();

            return valor * taxa;

        } catch (IOException | NullPointerException e) {
            System.err.println("Erro ao acessar API ou processar dados: " + e.getMessage());
            return -1;
        }
    }
}

