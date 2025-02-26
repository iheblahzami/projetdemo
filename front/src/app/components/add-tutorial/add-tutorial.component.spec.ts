import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddTutorialComponent } from './add-tutorial.component';
import { TutorialService } from 'src/app/services/tutorial.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AddTutorialComponent', () => {
  let component: AddTutorialComponent;
  let fixture: ComponentFixture<AddTutorialComponent>;
  let tutorialServiceSpy: jasmine.SpyObj<TutorialService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('TutorialService', ['create']);

    TestBed.configureTestingModule({
      declarations: [AddTutorialComponent],
      imports: [HttpClientTestingModule,FormsModule],
      providers: [{ provide: TutorialService, useValue: spy }]
    }).compileComponents();

    fixture = TestBed.createComponent(AddTutorialComponent);

    component = fixture.componentInstance;
    tutorialServiceSpy = TestBed.inject(TutorialService) as jasmine.SpyObj<TutorialService>;
  });

  it('should create tutorial and set submitted to true', () => {
    const mockResponse = { title: 'New', description: 'New Desc' };
    tutorialServiceSpy.create.and.returnValue(of(mockResponse));

    component.tutorial = { title: 'New', description: 'New Desc', published: false };
    component.saveTutorial();

    expect(tutorialServiceSpy.create).toHaveBeenCalledWith({
      title: 'New',
      description: 'New Desc'
    });
    expect(component.submitted).toBeTrue();
  });
});
