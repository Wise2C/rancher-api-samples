package com.wise2c.samples.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Environments extends CollectionResponse<Environment> {
}
