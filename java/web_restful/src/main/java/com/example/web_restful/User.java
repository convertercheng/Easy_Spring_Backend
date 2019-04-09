package com.example.web_restful;

import lombok.Getter;
import lombok.Setter;

public class User {
    private @Setter @Getter Long id;
    private @Setter @Getter String name;
    private @Setter @Getter Integer age;
}
