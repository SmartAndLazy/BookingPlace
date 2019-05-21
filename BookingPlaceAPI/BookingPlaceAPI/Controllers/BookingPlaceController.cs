using BookingPlaceAPI.Entities;
using BookingPlaceAPI.Helpers;
using BookingPlaceAPI.Models;
using BookingPlaceAPI.Services;
using BookingPlaceAPI.Stores;
using BookingPlaceAPI.ViewModels;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Controllers
{
    [Route("api/[controller]")]
    [Produces("application/json")]
    public class BookingPlaceController : Controller
    {
        private readonly IOrganizationStore _organizationStore;
        private readonly IUserStore _userStore;
        private readonly IYandexSearchService _yandexSearchService;

        public BookingPlaceController(IOrganizationStore organizationStore, IUserStore userStore, IYandexSearchService yandexSearchService)
        {
            _organizationStore = organizationStore;
            _userStore = userStore;
            _yandexSearchService = yandexSearchService;
        }

        /// <summary> Метод получения данных об организации </summary>
        /// <param name="name"></param>
        /// <param name="latitude"></param>
        /// <param name="longitude"></param>
        /// <returns></returns>
        [HttpGet(nameof(GetOrganizationData) + "/{name}/{latitude}/{longitude}")]
        public async Task<IActionResult> GetOrganizationData([FromRoute]string name, [FromRoute]double latitude, [FromRoute]double longitude)
        {
            try
            {
                var organization = await _organizationStore.GetOrganizationAsync(name, latitude, longitude);
                if (organization == null)
                {
                    var organizationYandexInfo = await _yandexSearchService.SearchOrganization(name);
                    organization = await _organizationStore.CreateOrganizationAsync(name, latitude, longitude, organizationYandexInfo);

                    if(organizationYandexInfo?.Links != null)
                    {
                        foreach(var link in organizationYandexInfo.Links)
                        {
                            var socialNetworkType = link.GetSocialNetworkType();
                            if ((int)socialNetworkType >= 1 || (int)socialNetworkType <= 3)
                                await _organizationStore.AddSocialNetworkAccountAsync(organization.Id, socialNetworkType, link.Href);
                        }
                    }
                }

                var socialNeгtworkAccounts = await _organizationStore.GetOrganizationSocialNetworkAccountsAsync(organization.Id);
                var ratings = (await _organizationStore.GetRatingByOrganization(organization.Id))?.ToList();

                var result = new OrganizationInfo
                {
                    Id = organization.Id,
                    Name = organization.Name,
                    RuName = organization.RuName,
                    Address = organization.Address,
                    Latitude = organization.Latitude,
                    Longitude = organization.Longitude,
                    Phone = organization.Phone,
                    CountOfFreePlacement = OrganizationHelper.GetCountEmptyPlacement(organization.CountOfAvailablePlacement ?? 15),
                    CountOfAvailablePlacement = organization.CountOfAvailablePlacement ?? 15,
                    Description = organization.Description,
                    WorkingTimes = new WorkingTimes
                    {
                        From = $"{organization.WorkingStartHour}:{organization.WorkingStartMinute}",
                        To = $"{organization.WorkingEndHour}:{organization.WorkingEndMinute}"
                    },
                    SocialNeгtworkAccounts = socialNeгtworkAccounts.Select(acc => new OrganizationSocialNetwork { Type = acc.Type, Url = acc.Url }),
                    Rating = organization.Rating ?? 0,
                    UserRatings = ratings.Select(rating => new UserRating
                    {
                        UserId = rating.UserId,
                        Name = rating.Name,
                        Surname = rating.Surname,
                        Phone = rating.Phone,
                        Rate = rating.Rating,
                        Text = rating.Text
                    }),
                    Categories = organization.Categories,
                    Url = organization.Url,
                    WorkingTimeStr = organization.WorkingTimeStr
                };

                return Ok(result);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        [HttpPost(nameof(UpdateDescription))]
        public async Task<IActionResult> UpdateDescription([FromBody]OrganizationDescriptionVM descriptionVM)
        {
            try
            {
                if(descriptionVM.OrganizationId.HasValue)
                    await _organizationStore.UpdateDescriptionAsync(descriptionVM.OrganizationId.Value, descriptionVM.Description);
                else
                    await _organizationStore.UpdateDescriptionAsync(descriptionVM.Name, descriptionVM.Latitude.Value, descriptionVM.Longitude.Value, descriptionVM.Description);

                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        [HttpPost(nameof(AddSocialNetworkAccount))]
        public async Task<IActionResult> AddSocialNetworkAccount([FromBody]OrganizationSocialNetworkUrlVM socialNetworkVM)
        {
            try
            {
                if (socialNetworkVM.OrganizationId.HasValue)
                    await _organizationStore.AddSocialNetworkAccountAsync(socialNetworkVM.OrganizationId.Value, socialNetworkVM.Type, socialNetworkVM.Url);
                else
                    await _organizationStore.AddSocialNetworkAccountAsync(socialNetworkVM);

                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        /// <summary> Метод регистрации пользователя </summary>
        /// <response code="200"> id пользователя </response>
        /// <response code="409 : 1"> Пользователь уже зарегистрирован </response>
        /// <response code="500"> Произошла ошибка сервера </response>
        /// <param name="registerUserVM"></param>
        /// <returns></returns>
        [HttpPost(nameof(RegisterUser))]
        public async Task<IActionResult> RegisterUser([FromBody]RegisterUserVM registerUserVM)
        {
            try
            {
                var user = await _userStore.GetUserByPhoneAsync(registerUserVM.Phone);
                if (user != null)
                    return Conflict(1);

                var result = await _userStore.CreateUserAsync(new UserEntity
                {
                    Name = registerUserVM.Name,
                    Surname = registerUserVM.Surname,
                    Phone = registerUserVM.Phone,
                    Password = registerUserVM.PasswordHash
                });

                return Ok(result);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        /// <summary> Метод логина пользователя </summary>
        /// <response code="200"> Информация о пользователе </response>
        /// <response code="401"> Неверно указан логин или пароль </response>
        /// <response code="500"> Произошла ошибка сервера </response>
        /// <param name="loginUserVM"></param>
        /// <returns></returns>
        [HttpPost(nameof(LoginUser))]
        public async Task<IActionResult> LoginUser([FromBody]LoginUserVM loginUserVM)
        {
            try
            {
                var user = await _userStore.GetUserAsync(loginUserVM.Phone, loginUserVM.PasswordHash);
                if (user == null)
                    return Unauthorized();

                return Ok(user);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        /// <summary> Оставить отзыв </summary>
        /// <response code="200"> Отзыв сохранен </response>
        /// <response code="400"> Некорректные входные данные </response>
        /// <response code="500"> Произошла ошибка сервера </response>
        /// <param name="rateVM"></param>
        /// <returns></returns>
        [HttpPost(nameof(Rate))]
        public async Task<IActionResult> Rate([FromBody]RateVM rateVM)
        {
            try
            {
                if (!rateVM.OrganizationId.HasValue || !rateVM.Rate.HasValue || !rateVM.UserId.HasValue)
                    return BadRequest();

                var user = await _userStore.GetUserAsync(rateVM.UserId.Value);
                if (user == null)
                    return BadRequest();

                var organization = await _organizationStore.GetOrganizationAsync(rateVM.OrganizationId.Value);
                if (organization == null)
                    return BadRequest();

                await _organizationStore.Rate(new RatingEntity
                {
                    OrganizationId = rateVM.OrganizationId.Value,
                    UserId = rateVM.UserId.Value,
                    Rating = rateVM.Rate.Value,
                    Text = rateVM.Text
                });

                var ratings = (await _organizationStore.GetRatingByOrganization(rateVM.OrganizationId.Value))?.ToList();
                var averageRating = ratings.Average(rating => rating.Rating);

                await _organizationStore.UpdateRatingAsync(rateVM.OrganizationId.Value, averageRating);
                var socialNeгtworkAccounts = await _organizationStore.GetOrganizationSocialNetworkAccountsAsync(organization.Id);

                var result = new OrganizationInfo
                {
                    Id = organization.Id,
                    Name = organization.Name,
                    RuName = organization.RuName,
                    Address = organization.Address,
                    Latitude = organization.Latitude,
                    Longitude = organization.Longitude,
                    Description = organization.Description,
                    Phone = organization.Phone,
                    Rating = averageRating,
                    CountOfAvailablePlacement = organization.CountOfAvailablePlacement ?? 15,
                    UserRatings = ratings.Select(rating => new UserRating
                    {
                        UserId = rating.UserId,
                        Name = rating.Name,
                        Surname = rating.Surname,
                        Phone = rating.Phone,
                        Rate = rating.Rating,
                        Text = rating.Text
                    }),
                    WorkingTimes = new WorkingTimes
                    {
                        From = $"{organization.WorkingStartHour}:{organization.WorkingStartMinute}",
                        To = $"{organization.WorkingEndHour}:{organization.WorkingEndMinute}"
                    },
                    SocialNeгtworkAccounts = socialNeгtworkAccounts.Select(acc => new OrganizationSocialNetwork { Type = acc.Type, Url = acc.Url }),
                    CountOfFreePlacement = OrganizationHelper.GetCountEmptyPlacement(organization.CountOfAvailablePlacement ?? 15),
                    Categories = organization.Categories,
                    Url = organization.Url,
                    WorkingTimeStr = organization.WorkingTimeStr
                };

                return Ok(result);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        /// <summary> Забронировать </summary>
        /// <response code="200"> Забронировано </response>
        /// <response code="400"> Некорректные входные данные </response>
        /// <response code="409 : 1"> Организация не найдена по указанному Id </response>
        /// <response code="409 : 2"> Некорректные входные данные </response>
        /// <response code="500"> Произошла ошибка сервера </response>
        /// <param name="bookingPlaceVM"></param>
        /// <returns></returns>
        [HttpPost(nameof(BookingPlace))]
        public async Task<IActionResult> BookingPlace([FromBody]BookingPlaceVM bookingPlaceVM)
        {
            try
            {
                if (!bookingPlaceVM.OrganizationId.HasValue || bookingPlaceVM.OrganizationId.Value <= 0)
                    return BadRequest();
                if (!TimeSpan.TryParse(bookingPlaceVM.FromTime, out var time))
                    return BadRequest();
                if (!DateTime.TryParse(bookingPlaceVM.Date, out var date))
                    return BadRequest();

                var organization = await _organizationStore.GetOrganizationAsync(bookingPlaceVM.OrganizationId.Value);
                if (organization == null)
                    return Conflict(1);

                var bookingDetail = await _organizationStore.GetOrganizationBookingPlacement(bookingPlaceVM.OrganizationId.Value, date);
                var detailInfo = ModelConverter.GetOrganizationBookingDetail(organization, bookingDetail.ToList());

                var posssibleBookingPlaces = detailInfo.GetPosssibleBookingPlaces(time);
                if (posssibleBookingPlaces == null || posssibleBookingPlaces.Count == 0)
                    return Conflict(2);
                if (posssibleBookingPlaces.Any(el => el.NumberOfFreePlacement < bookingPlaceVM.NumberOfTables))
                    return Conflict(3);

                await _organizationStore.BookingPlace(new OrganizationBookingPlaceEntity
                {
                    Name = bookingPlaceVM.Name,
                    Surname = bookingPlaceVM.Surname,
                    Phone = bookingPlaceVM.Phone,
                    OrganizationId = bookingPlaceVM.OrganizationId.Value,
                    NumberOfTable = bookingPlaceVM.NumberOfTables.Value,
                    Date = date,
                    BookingStartHour = time.Hours,
                    BookingStartMinute = time.Minutes
                });

                return Ok();
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }

        /// <summary> Метод получения детальной информации по бронированию на день для организации </summary>
        /// <param name="organizationId"></param>
        /// <param name="day"></param>
        /// <response code="400"> Некорректные входные данные </response>
        /// <response code="409 : 1"> Организация не найдена по указанному Id </response>
        /// <response code="500"> Произошла ошибка сервера </response>
        /// <returns></returns>
        [HttpPost(nameof(GetOrganizationBookingDetail) + "/{organizationId}/{day}")]
        public async Task<IActionResult> GetOrganizationBookingDetail([FromRoute]int organizationId, [FromRoute]string day)
        {
            try
            {
                if (organizationId <= 0)
                    return BadRequest();

                if (!DateTime.TryParse(day, out var date))
                    return BadRequest();

                var organization = await _organizationStore.GetOrganizationAsync(organizationId);
                if (organization == null)
                    return Conflict(1);

                var bookingDetail = await _organizationStore.GetOrganizationBookingPlacement(organizationId, date);
                var result = ModelConverter.GetOrganizationBookingDetail(organization, bookingDetail.ToList());

                return Ok(result);
            }
            catch (Exception ex)
            {
                return StatusCode((int)HttpStatusCode.InternalServerError);
            }
        }
    }
}
