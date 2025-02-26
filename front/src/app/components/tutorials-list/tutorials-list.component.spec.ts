import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TutorialsListComponent } from './tutorials-list.component';
import { TutorialService } from 'src/app/services/tutorial.service';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TutorialDetailsComponent } from '../tutorial-details/tutorial-details.component'; // Add this import
import { ActivatedRoute } from '@angular/router'; // Add this import

describe('TutorialsListComponent', () => {
  let component: TutorialsListComponent;
  let fixture: ComponentFixture<TutorialsListComponent>;
  let tutorialServiceSpy: jasmine.SpyObj<TutorialService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('TutorialService', ['getAll']);

    TestBed.configureTestingModule({
      declarations: [TutorialsListComponent, TutorialDetailsComponent], // Add TutorialDetailsComponent here
      imports: [HttpClientTestingModule],
      providers: [
        { provide: TutorialService, useValue: spy },
        {
          provide: ActivatedRoute, // Mock ActivatedRoute
          useValue: {
            params: of({ id: '1' }), // Mock params with an observable
            snapshot: { params: { id: '1' } } // Mock snapshot
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TutorialsListComponent);
    component = fixture.componentInstance;
    tutorialServiceSpy = TestBed.inject(TutorialService) as jasmine.SpyObj<TutorialService>;
  });

  it('should retrieve tutorials on init', () => {
    const mockTutorials = [
      { id: 1, title: 'Test1', description: 'Desc1', published: true },
      { id: 2, title: 'Test2', description: 'Desc2', published: false }
    ];
    tutorialServiceSpy.getAll.and.returnValue(of(mockTutorials));

    component.ngOnInit();

    expect(component.tutorials).toEqual(mockTutorials);
    expect(tutorialServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should set active tutorial', () => {
    const tutorial = { id: 1, title: 'Test1', description: 'Desc1', published: true };

    component.setActiveTutorial(tutorial, 0);

    expect(component.currentTutorial).toEqual(tutorial);
    expect(component.currentIndex).toBe(0);
  });
});
