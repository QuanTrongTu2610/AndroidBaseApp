package com.example.androidbaseapp.common.converter

import com.example.androidbaseapp.data.local.entity.ArticleEntity
import com.example.androidbaseapp.data.local.entity.BasicCountryEntity
import com.example.androidbaseapp.data.local.entity.DetailCountryEntity
import com.example.androidbaseapp.data.local.entity.LoadingKeyEntity
import com.example.androidbaseapp.data.remote.entity.ArticleResult
import com.example.androidbaseapp.data.remote.entity.CovidBasicCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidDetailCountryResult
import com.example.androidbaseapp.data.remote.entity.CovidWorldWipResult
import com.example.androidbaseapp.data.repositories.model.ArticleModel
import com.example.androidbaseapp.data.repositories.model.BasicCountryModel
import com.example.androidbaseapp.data.repositories.model.DetailCountryModel
import com.example.androidbaseapp.data.repositories.model.LoadingKeyModel
import com.example.androidbaseapp.data.repositories.model.WorldWipModel

fun CovidBasicCountryResult.toBasicCountryModel() = BasicCountryModel(
    fullName = country,
    shortName = slug,
    abbreviation = iSO2,
)

fun DetailCountryEntity.toDetailCountryModel() = DetailCountryModel(
    id = id,
    countryId = this.countryId,
    city = city,
    cityCode = cityCode,
    country = country,
    countryCode = countryCode,
    lat = lat,
    lon = lon,
    province = province,
    confirmed = confirmed,
    active = active,
    deaths = deaths,
    recovered = recovered,
    date = date,
)

fun CovidWorldWipResult.toWorldWipModel() = WorldWipModel(
    newConfirmed = newConfirmed,
    totalConfirmed = totalConfirmed,
    newDeaths = newDeaths,
    newRecovered = newRecovered,
    totalRecovered = totalRecovered,
    totalDeaths = totalDeaths,
    date = date
)

fun BasicCountryEntity.toBasicCountryModel() = BasicCountryModel(
    id = id,
    fullName = fullName,
    shortName = shortName,
    abbreviation = abbreviation
)

fun LoadingKeyEntity.toLoadingKeyModel() = LoadingKeyModel(
    loadingKeyId = id,
    prevKey = prevKey,
    nextKey = nextKey,
    keyType = keyType
)

fun CovidDetailCountryResult.toDetailCountryEntity() = DetailCountryEntity(
    id = id,
    city = city,
    cityCode = cityCode,
    country = country,
    countryCode = countryCode,
    lat = lat,
    lon = lon,
    province = province,
    confirmed = confirmed,
    active = active,
    deaths = deaths,
    recovered = recovered,
    date = date,
)

fun LoadingKeyModel.toLoadingKeyEntity() = LoadingKeyEntity(
    id = loadingKeyId,
    prevKey = prevKey,
    nextKey = nextKey,
    keyType = keyType
)

fun BasicCountryModel.toLocalBasicCountryEntity() = BasicCountryEntity(
    id = id,
    fullName = fullName,
    shortName = shortName,
    abbreviation = abbreviation,
)

fun DetailCountryModel.toLocalDetailCountryEntity() = DetailCountryEntity(
    id = id,
    countryId = countryId,
    city = city,
    cityCode = cityCode,
    country = country,
    countryCode = countryCode,
    lat = lat,
    lon = lon,
    province = province,
    confirmed = confirmed,
    active = active,
    deaths = deaths,
    recovered = recovered,
    date = date,
)

fun ArticleResult.toArticleEntity() = ArticleEntity(
    author = author,
    content = content,
    publishedAt = publishedAt,
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun ArticleModel.toArticleEntity() = ArticleEntity(
    author = author,
    content = content,
    publishedAt = publishedAt,
    title = title,
    url = url,
    urlToImage = urlToImage,
    pageId = pageId
)

fun ArticleEntity.toArticleModel() = ArticleModel(
    author = author,
    content = content,
    publishedAt = publishedAt,
    title = title,
    url = url,
    urlToImage = urlToImage,
    pageId = pageId
)